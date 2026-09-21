package dev.felix.ellice.account;

public final class AccountCodeService extends RuntimeException {
  private final String text;

  public AccountCodeService(String currentText, String nextText) {
    super(nextText);
    this.text = currentText;
  }

  public String code() {
    return this.text;
  }
}
