import { overlay } from "../common/config";
import showAlert from "../components/show-alert-bar";
import { setupModal } from "../common/helpers";

// Function that displays alerts based on login parameters
export function handleLoginAlerts() {
  const urlParams = new URLSearchParams(window.location.search);

  if (urlParams.has("error")) {
    showAlert("Invalid username or password. Please try again.", "error");
  }

  if (urlParams.has("logout")) {
    showAlert("You have logged out successfully.", "success");
  }
}

// Helper function to remove login-related URL parameters
export function removeLoginParams() {
  const url = new URL(window.location);
  const paramsToRemove = ["error", "logout"];
  let shouldUpdate = false;

  paramsToRemove.forEach((param) => {
    if (url.searchParams.has(param)) {
      url.searchParams.delete(param);
      shouldUpdate = true;
    }
  });

  if (shouldUpdate) {
    history.replaceState(null, "", url.pathname + url.search);
  }
}

export function showLoginInfoModal() {
  setupModal({
    modalSelector: ".modal-login",
    openBtnSelector: ".login-info",
  });
}

export function showForgotPasswordModal() {
  setupModal({
    modalSelector: ".modal__forgot-password",
    openBtnSelector: ".login-link__forgot-password",
  });
}

export function setupNumberValidation() {
  const numberInput = document.getElementById("numberInput");
  const confirmNumberInput = document.getElementById("confirmNumberInput");
  const form = document.querySelector(".forgot-password__form");
  const submitButton = form.querySelector(".btn__forgot-password");

  // Check if the required fields exist in the DOM
  if (!numberInput || !confirmNumberInput || !form || !submitButton) {
    console.warn("Required forgot password fields not found.");
    return;
  }

  // Create and insert the error message element
  const errorMessage = document.createElement("p");
  errorMessage.textContent = "Numbers do not match!";
  errorMessage.classList.add("error-message");
  errorMessage.style.display = "none";
  confirmNumberInput.insertAdjacentElement("afterend", errorMessage);

  function validateNumbers() {
    const numberVal = numberInput.value.toUpperCase();
    const confirmVal = confirmNumberInput.value.toUpperCase();

    if (numberInput.value || confirmNumberInput.value) {
      if (numberVal !== confirmVal) {
        confirmNumberInput.classList.add("input-error");
        errorMessage.style.display = "block";
        confirmNumberInput.placeholder = "Numbers do not match!";
        submitButton.disabled = true;
      } else {
        confirmNumberInput.classList.remove("input-error");
        errorMessage.style.display = "none";
        confirmNumberInput.placeholder = "Re-enter your number";
        submitButton.disabled = false;
      }
    } else {
      confirmNumberInput.classList.remove("input-error");
      errorMessage.style.display = "none";
      confirmNumberInput.placeholder = "Re-enter your number";
      submitButton.disabled = true;
    }
  }

  form.addEventListener("submit", function (e) {
    const numberVal = numberInput.value.toUpperCase();
    const confirmVal = confirmNumberInput.value.toUpperCase();
    if (numberVal !== confirmVal) {
      e.preventDefault();
      confirmNumberInput.classList.add("input-error");
      errorMessage.style.display = "block";
    }
  });

  numberInput.addEventListener("input", validateNumbers);
  confirmNumberInput.addEventListener("input", validateNumbers);
}
