export function setupPasswordValidation() {
  const newPassword = document.getElementById("newPassword");
  const confirmNewPassword = document.getElementById("confirmNewPassword");
  const form = document.querySelector(".change-password-form");
  const submitButton = form.querySelector(".change-password-button");

  // Check if the required fields exist in the DOM
  if (!newPassword || !confirmNewPassword || !form || !submitButton) {
    console.warn("Required fields not found.");
    return;
  }

  // Create and insert the error message element
  const errorMessage = document.createElement("p");
  errorMessage.textContent = "Passwords do not match!";
  errorMessage.classList.add("error-message");
  errorMessage.style.display = "none";
  confirmNewPassword.insertAdjacentElement("afterend", errorMessage);

  // Function to validate if the passwords match
  function validatePasswords() {
    // Check if either field has a value
    if (newPassword.value || confirmNewPassword.value) {
      if (newPassword.value !== confirmNewPassword.value) {
        // If passwords do not match, show the error and disable the button
        confirmNewPassword.classList.add("input-error");
        errorMessage.style.display = "block";
        confirmNewPassword.placeholder = "Passwords do not match!";
        submitButton.disabled = true;
      } else {
        // If passwords match, hide the error and enable the button
        confirmNewPassword.classList.remove("input-error");
        errorMessage.style.display = "none";
        confirmNewPassword.placeholder = "Confirm your new password";
        submitButton.disabled = false;
      }
    } else {
      // If both fields are empty, reset the error and disable the button
      confirmNewPassword.classList.remove("input-error");
      errorMessage.style.display = "none";
      confirmNewPassword.placeholder = "Confirm your new password";
      submitButton.disabled = true;
    }
  }

  // Prevent form submission if passwords do not match
  form.addEventListener("submit", function (event) {
    if (newPassword.value !== confirmNewPassword.value) {
      event.preventDefault(); // Stop form submission
      confirmNewPassword.classList.add("input-error");
      errorMessage.style.display = "block";
    }
  });

  // Add event listeners to trigger validation while typing
  newPassword.addEventListener("input", validatePasswords);
  confirmNewPassword.addEventListener("input", validatePasswords);
}
