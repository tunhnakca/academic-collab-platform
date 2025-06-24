export function setupResetPasswordValidation() {
  const passwordInput = document.getElementById("resetNewPasswordInput");
  const confirmPasswordInput = document.getElementById(
    "resetConfirmNewPasswordInput"
  );
  const form = document.querySelector(".reset-password__form");
  const submitButton = form.querySelector(".btn__reset-password");

  // Check if the required fields exist in the DOM
  if (!passwordInput || !confirmPasswordInput || !form || !submitButton) {
    return;
  }

  // Create and insert the error message element
  const errorMessage = document.createElement("p");
  errorMessage.textContent = "Passwords do not match!";
  errorMessage.classList.add("error-message");
  errorMessage.style.display = "none";
  confirmPasswordInput.insertAdjacentElement("afterend", errorMessage);

  function validatePasswords() {
    if (passwordInput.value || confirmPasswordInput.value) {
      if (passwordInput.value !== confirmPasswordInput.value) {
        confirmPasswordInput.classList.add("input-error");
        errorMessage.style.display = "block";
        confirmPasswordInput.placeholder = "Passwords do not match!";
        submitButton.disabled = true;
      } else {
        confirmPasswordInput.classList.remove("input-error");
        errorMessage.style.display = "none";
        confirmPasswordInput.placeholder = "Re-enter your password";
        submitButton.disabled = false;
      }
    } else {
      confirmPasswordInput.classList.remove("input-error");
      errorMessage.style.display = "none";
      confirmPasswordInput.placeholder = "Re-enter your password";
      submitButton.disabled = true;
    }
  }

  form.addEventListener("submit", function (e) {
    if (passwordInput.value !== confirmPasswordInput.value) {
      e.preventDefault();
      confirmPasswordInput.classList.add("input-error");
      errorMessage.style.display = "block";
    }
  });

  passwordInput.addEventListener("input", validatePasswords);
  confirmPasswordInput.addEventListener("input", validatePasswords);
}
