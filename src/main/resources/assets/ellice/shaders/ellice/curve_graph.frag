#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vUV;

uniform vec4 uRect;       
uniform vec4 uPlot;       
uniform float uRadius;
uniform int uCurveType;   
uniform vec4 uBezier;     
uniform int uSteps;
uniform int uStepMode;    
uniform int uActiveHandle;
uniform float uOpacity;
uniform float uEdgeSoftness;
uniform float uMetricScale;
uniform float uCurveWidth;
uniform float uCurveFringeWidth;

uniform vec4 uBackgroundColor;
uniform vec4 uBorderColor;
uniform vec4 uGridColor;
uniform vec4 uAxisColor;
uniform vec4 uCurveColor;
uniform vec4 uCurveFringeColor;
uniform vec4 uFillColor;
uniform vec4 uGuideColor;
uniform vec4 uHandleColor;
uniform vec4 uActiveHandleColor;
uniform vec4 uHandleBorderColor;
uniform vec4 uEndpointColor;

out vec4 fragColor;

float hermite(float t, float y0, float y1, float m0, float m1) {
    float t2 = t * t;
    float t3 = t2 * t;
    return (2.0 * t3 - 3.0 * t2 + 1.0) * y0
        + (t3 - 2.0 * t2 + t) * m0
        + (-2.0 * t3 + 3.0 * t2) * y1
        + (t3 - t2) * m1;
}

float hermiteD1(float t, float y0, float y1, float m0, float m1) {
    float t2 = t * t;
    return (6.0 * t2 - 6.0 * t) * y0
        + (3.0 * t2 - 4.0 * t + 1.0) * m0
        + (-6.0 * t2 + 6.0 * t) * y1
        + (3.0 * t2 - 2.0 * t) * m1;
}

float hermiteD2(float t, float y0, float y1, float m0, float m1) {
    return (12.0 * t - 6.0) * y0
        + (6.0 * t - 4.0) * m0
        + (-12.0 * t + 6.0) * y1
        + (6.0 * t - 2.0) * m1;
}



vec3 compressYData(float rawY) {
    float y = clamp(rawY, -2.0, 2.0);
    if (y <= 0.0) {
        float t = (y + 2.0) * 0.5;
        return vec3(
            hermite(t, 0.0, 0.25, 0.0, 3.0 / 7.0),
            hermiteD1(t, 0.0, 0.25, 0.0, 3.0 / 7.0) * 0.5,
            hermiteD2(t, 0.0, 0.25, 0.0, 3.0 / 7.0) * 0.25
        );
    }
    if (y <= 1.0) {
        return vec3(
            hermite(y, 0.25, 0.75, 3.0 / 14.0, 1.0 / 3.0),
            hermiteD1(y, 0.25, 0.75, 3.0 / 14.0, 1.0 / 3.0),
            hermiteD2(y, 0.25, 0.75, 3.0 / 14.0, 1.0 / 3.0)
        );
    }
    float t = y - 1.0;
    return vec3(
        hermite(t, 0.75, 1.0, 1.0 / 3.0, 0.125),
        hermiteD1(t, 0.75, 1.0, 1.0 / 3.0, 0.125),
        hermiteD2(t, 0.75, 1.0, 1.0 / 3.0, 0.125)
    );
}

float cubic(float t, float p1, float p2) {
    float inverse = 1.0 - t;
    return 3.0 * inverse * inverse * t * p1
        + 3.0 * inverse * t * t * p2
        + t * t * t;
}

float cubicD1(float t, float p1, float p2) {
    float inverse = 1.0 - t;
    return 3.0 * inverse * inverse * p1
        + 6.0 * inverse * t * (p2 - p1)
        + 3.0 * t * t * (1.0 - p2);
}

float cubicD2(float t, float p1, float p2) {
    return 6.0 * (1.0 - t) * (p2 - 2.0 * p1)
        + 6.0 * t * (1.0 - 2.0 * p2 + p1);
}

float roundedBoxSDF(vec2 point, vec2 halfSize, float radius) {
    float r = clamp(radius, 0.0, min(halfSize.x, halfSize.y));
    vec2 q = abs(point) - halfSize + vec2(r);
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - r;
}

float sdSegment(vec2 point, vec2 start, vec2 end) {
    vec2 segment = end - start;
    float denominator = max(dot(segment, segment), 0.000001);
    float t = clamp(dot(point - start, segment) / denominator, 0.0, 1.0);
    return length(point - start - segment * t);
}

float strokeMask(float distanceToCenter, float halfWidth) {
    
    
    
    
    float aa = max(0.45, fwidth(distanceToCenter) * 0.65);
    return 1.0 - smoothstep(halfWidth - aa, halfWidth + aa, distanceToCenter);
}

float discMask(vec2 point, vec2 center, float radius) {
    float distanceToCenter = length(point - center);
    float aa = max(0.65, fwidth(distanceToCenter));
    return 1.0 - smoothstep(radius - aa, radius + aa, distanceToCenter);
}

float rectMask(vec2 point, vec2 origin, vec2 size) {
    vec2 halfSize = size * 0.5;
    vec2 outside = abs(point - origin - halfSize) - halfSize;
    float distanceToRect = max(outside.x, outside.y);
    float aa = max(0.65, fwidth(distanceToRect));
    return 1.0 - smoothstep(-aa, aa, distanceToRect);
}

vec4 premultiplied(vec4 straightColor, float coverage) {
    float alpha = straightColor.a * clamp(coverage, 0.0, 1.0);
    return vec4(straightColor.rgb * alpha, alpha);
}

vec4 over(vec4 top, vec4 bottom) {
    return top + bottom * (1.0 - top.a);
}

vec2 plotOrigin() {
    return uPlot.xy - uRect.xy;
}

float mapRawY(float rawY) {
    return plotOrigin().y + (1.0 - compressYData(rawY).x) * uPlot.w;
}

float solveBezierX(float normalizedX) {
    float target = clamp(normalizedX, 0.0, 1.0);
    if (target <= 0.0) return 0.0;
    if (target >= 1.0) return 1.0;
    float low = 0.0;
    float high = 1.0;
    float t = target;
    for (int iteration = 0; iteration < 7; iteration++) {
        float value = cubic(t, uBezier.x, uBezier.z);
        float error = value - target;
        if (abs(error) <= 0.000001) return t;
        if (error < 0.0) low = t;
        else high = t;
        float slope = cubicD1(t, uBezier.x, uBezier.z);
        float candidate = t - error / max(abs(slope), 0.00001);
        if (candidate < low || candidate > high) candidate = 0.5 * (low + high);
        t = candidate;
    }
    return clamp(t, 0.0, 1.0);
}

void parametricData(float t, out vec2 point, out vec2 derivative,
                    out vec2 secondDerivative) {
    float rawX;
    float rawY;
    float dx;
    float dy;
    float ddx;
    float ddy;
    if (uCurveType == 0) {
        rawX = t;
        rawY = t;
        dx = 1.0;
        dy = 1.0;
        ddx = 0.0;
        ddy = 0.0;
    } else {
        rawX = cubic(t, uBezier.x, uBezier.z);
        rawY = cubic(t, uBezier.y, uBezier.w);
        dx = cubicD1(t, uBezier.x, uBezier.z);
        dy = cubicD1(t, uBezier.y, uBezier.w);
        ddx = cubicD2(t, uBezier.x, uBezier.z);
        ddy = cubicD2(t, uBezier.y, uBezier.w);
    }
    vec3 compressed = compressYData(rawY);
    vec2 origin = plotOrigin();
    point = vec2(origin.x + rawX * uPlot.z,
        origin.y + (1.0 - compressed.x) * uPlot.w);
    derivative = vec2(dx * uPlot.z,
        -compressed.y * dy * uPlot.w);
    secondDerivative = vec2(ddx * uPlot.z,
        -(compressed.z * dy * dy + compressed.y * ddy) * uPlot.w);
}

float parametricDistance(vec2 point, out float curveY) {
    vec2 origin = plotOrigin();
    float normalizedX = clamp((point.x - origin.x) / max(uPlot.z, 0.0001), 0.0, 1.0);
    float t = uCurveType == 0 ? normalizedX : solveBezierX(normalizedX);
    vec2 curvePoint;
    vec2 derivative;
    vec2 secondDerivative;
    parametricData(t, curvePoint, derivative, secondDerivative);
    curveY = curvePoint.y;

    float tangentLength = max(length(derivative), 0.0001);
    float approximateDistance = abs(derivative.x * (point.y - curvePoint.y)
        - derivative.y * (point.x - curvePoint.x)) / tangentLength;
    float refineBand = max(12.0 * uMetricScale, uCurveFringeWidth + 5.0);
    if (approximateDistance <= refineBand) {
        for (int iteration = 0; iteration < 3; iteration++) {
            parametricData(t, curvePoint, derivative, secondDerivative);
            vec2 offset = curvePoint - point;
            float denominator = dot(derivative, derivative)
                + dot(offset, secondDerivative);
            if (abs(denominator) > 0.0001) {
                float delta = clamp(dot(offset, derivative) / denominator, -0.2, 0.2);
                t = clamp(t - delta, 0.0, 1.0);
            }
        }
        parametricData(t, curvePoint, derivative, secondDerivative);
    }
    return length(point - curvePoint);
}

float stepSample(float normalizedX) {
    float x = clamp(normalizedX, 0.0, 1.0);
    int count = max(uSteps, 1);
    int current = int(floor(x * float(count)));
    if (uStepMode == 0 || uStepMode == 3) current += 1;
    int jumps = count;
    if (uStepMode == 2) jumps = max(count - 1, 1);
    else if (uStepMode == 3) jumps = count + 1;
    return clamp(float(current) / float(jumps), 0.0, 1.0);
}

float stepCellLevel(int cell) {
    int count = max(uSteps, 1);
    int bounded = clamp(cell, 0, count - 1);
    return stepSample((float(bounded) + 0.5) / float(count));
}

float stepHorizontalDistance(vec2 point, int cell) {
    int count = max(uSteps, 1);
    int bounded = clamp(cell, 0, count - 1);
    vec2 origin = plotOrigin();
    float startX = origin.x + float(bounded) / float(count) * uPlot.z;
    float endX = origin.x + float(bounded + 1) / float(count) * uPlot.z;
    float y = mapRawY(stepCellLevel(bounded));
    return sdSegment(point, vec2(startX, y), vec2(endX, y));
}

float stepVerticalDistance(vec2 point, int boundary) {
    int count = max(uSteps, 1);
    int k = clamp(boundary, 1, count);
    float left = stepCellLevel(k - 1);
    float right = k < count ? stepCellLevel(k) : stepSample(1.0);
    vec2 origin = plotOrigin();
    float x = origin.x + float(k) / float(count) * uPlot.z;
    return sdSegment(point, vec2(x, mapRawY(left)), vec2(x, mapRawY(right)));
}

float stepsDistance(vec2 point, out float curveY) {
    vec2 origin = plotOrigin();
    float normalizedX = clamp((point.x - origin.x) / max(uPlot.z, 0.0001), 0.0, 1.0);
    int count = max(uSteps, 1);
    int cell = min(int(floor(normalizedX * float(count))), count - 1);
    float distanceToCurve = stepHorizontalDistance(point, cell);
    distanceToCurve = min(distanceToCurve, stepHorizontalDistance(point, cell - 1));
    distanceToCurve = min(distanceToCurve, stepHorizontalDistance(point, cell + 1));
    int leftBoundary = int(floor(normalizedX * float(count)));
    distanceToCurve = min(distanceToCurve, stepVerticalDistance(point, leftBoundary));
    distanceToCurve = min(distanceToCurve, stepVerticalDistance(point, leftBoundary + 1));
    curveY = mapRawY(stepSample(normalizedX));
    return distanceToCurve;
}

float dashedGuide(vec2 point, vec2 start, vec2 end) {
    vec2 segment = end - start;
    float lengthInPixels = max(length(segment), 0.0001);
    float along = clamp(dot(point - start, segment) /
        (lengthInPixels * lengthInPixels), 0.0, 1.0) * lengthInPixels;
    float distanceToGuide = sdSegment(point, start, end);
    float period = max(2.0, 7.0 * uMetricScale);
    float dashLength = period * 0.56;
    float centeredPhase = abs(mod(along + period * 0.5, period) - period * 0.5);
    float dashAA = max(0.65, fwidth(along));
    float dash = 1.0 - smoothstep(dashLength * 0.5 - dashAA,
        dashLength * 0.5 + dashAA, centeredPhase);
    return strokeMask(distanceToGuide, 0.48 * uMetricScale) * dash;
}

void main() {
    vec2 point = vUV * uRect.zw;
    vec2 graphHalf = uRect.zw * 0.5;
    float graphDistance = roundedBoxSDF(point - graphHalf, graphHalf, uRadius);
    float surfaceAA = max(0.65 + uEdgeSoftness, fwidth(graphDistance));
    float surface = 1.0 - smoothstep(-surfaceAA, surfaceAA, graphDistance);

    
    
    vec4 composed = premultiplied(uBackgroundColor, 1.0);

    vec2 origin = plotOrigin();
    float plot = rectMask(point, origin, uPlot.zw);
    float curveY;
    float curveDistance;
    if (uCurveType == 2) curveDistance = stepsDistance(point, curveY);
    else curveDistance = parametricDistance(point, curveY);

    float baselineY = mapRawY(0.0);
    float fillTop = min(curveY, baselineY);
    float fillBottom = max(curveY, baselineY);
    float fillEdgeAA = max(0.65, fwidth(curveY - point.y));
    float betweenCurveAndBaseline =
        smoothstep(fillTop - fillEdgeAA, fillTop + fillEdgeAA, point.y)
        * (1.0 - smoothstep(fillBottom - fillEdgeAA,
            fillBottom + fillEdgeAA, point.y));
    float fillPresence = smoothstep(0.0, fillEdgeAA * 2.0,
        abs(baselineY - curveY));
    composed = over(premultiplied(uFillColor,
        plot * betweenCurveAndBaseline * fillPresence), composed);

    float lineHalfWidth = 0.40 * uMetricScale;
    float gridDistance = 100000.0;
    gridDistance = min(gridDistance, abs(point.x - (origin.x + uPlot.z * 0.25)));
    gridDistance = min(gridDistance, abs(point.x - (origin.x + uPlot.z * 0.50)));
    gridDistance = min(gridDistance, abs(point.x - (origin.x + uPlot.z * 0.75)));
    gridDistance = min(gridDistance, abs(point.y - (origin.y + uPlot.w * 0.50)));
    float grid = strokeMask(gridDistance, lineHalfWidth) * plot;
    composed = over(premultiplied(uGridColor, grid), composed);

    float axisDistance = min(
        abs(point.y - (origin.y + uPlot.w * 0.25)),
        abs(point.y - (origin.y + uPlot.w * 0.75))
    );
    vec2 plotHalf = uPlot.zw * 0.5;
    float plotSignedDistance = roundedBoxSDF(
        point - (origin + plotHalf), plotHalf, 0.0);
    float plotEdgeDistance = abs(plotSignedDistance + lineHalfWidth);
    float axes = max(strokeMask(axisDistance, lineHalfWidth) * plot,
        strokeMask(plotEdgeDistance, lineHalfWidth));
    composed = over(premultiplied(uAxisColor, axes), composed);

    vec2 startPoint;
    vec2 startDerivative;
    vec2 startSecond;
    vec2 endPoint;
    vec2 endDerivative;
    vec2 endSecond;
    if (uCurveType == 2) {
        startPoint = vec2(origin.x, mapRawY(stepSample(0.0)));
        endPoint = vec2(origin.x + uPlot.z, mapRawY(stepSample(1.0)));
    } else {
        parametricData(0.0, startPoint, startDerivative, startSecond);
        parametricData(1.0, endPoint, endDerivative, endSecond);
    }

    vec2 firstHandle = vec2(origin.x + uBezier.x * uPlot.z, mapRawY(uBezier.y));
    vec2 secondHandle = vec2(origin.x + uBezier.z * uPlot.z, mapRawY(uBezier.w));
    if (uCurveType == 1) {
        
        
        float linearReference = dashedGuide(point, startPoint, endPoint);
        composed = over(premultiplied(uGuideColor,
            linearReference * 0.28), composed);
        float guides = max(dashedGuide(point, startPoint, firstHandle),
            dashedGuide(point, endPoint, secondHandle));
        composed = over(premultiplied(uGuideColor, guides), composed);
    }

    float fringe = strokeMask(curveDistance, uCurveFringeWidth * 0.5);
    float core = strokeMask(curveDistance, uCurveWidth * 0.5);
    
    
    float fringeShell = max(0.0, fringe - core) * 0.72;
    composed = over(premultiplied(uCurveFringeColor, fringeShell), composed);
    composed = over(premultiplied(uCurveColor, core), composed);

    float endpointRadius = 2.15 * uMetricScale;
    float endpoints = max(discMask(point, startPoint, endpointRadius),
        discMask(point, endPoint, endpointRadius));
    composed = over(premultiplied(uEndpointColor, endpoints), composed);

    if (uCurveType == 1) {
        float handleRadius = 3.65 * uMetricScale;
        float borderRadius = handleRadius + 0.95 * uMetricScale;
        float handleBorders = max(discMask(point, firstHandle, borderRadius),
            discMask(point, secondHandle, borderRadius));
        composed = over(premultiplied(uHandleBorderColor, handleBorders), composed);

        float firstHandleMask = discMask(point, firstHandle,
            handleRadius + (uActiveHandle == 1 ? 0.55 * uMetricScale : 0.0));
        float secondHandleMask = discMask(point, secondHandle,
            handleRadius + (uActiveHandle == 2 ? 0.55 * uMetricScale : 0.0));
        composed = over(premultiplied(
            uActiveHandle == 1 ? uActiveHandleColor : uHandleColor,
            firstHandleMask), composed);
        composed = over(premultiplied(
            uActiveHandle == 2 ? uActiveHandleColor : uHandleColor,
            secondHandleMask), composed);

        if (uActiveHandle != 0) {
            vec2 activeCenter = uActiveHandle == 1 ? firstHandle : secondHandle;
            float focusOuter = discMask(point, activeCenter, 6.8 * uMetricScale);
            float focusInner = discMask(point, activeCenter, 5.5 * uMetricScale);
            float focusRing = max(0.0, focusOuter - focusInner);
            composed = over(premultiplied(uCurveFringeColor, focusRing), composed);
        }
    }

    float borderDistance = abs(graphDistance + 0.55 * uMetricScale);
    float border = strokeMask(borderDistance, 0.55 * uMetricScale);
    composed = over(premultiplied(uBorderColor, border), composed);

    float finalMask = surface * uOpacity * elliceRoundedClipMask();
    fragColor = composed * finalMask;
}
