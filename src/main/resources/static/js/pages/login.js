import { overlay } from "../common/config";
import showAlert from "../components/show-alert-bar";

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
  const modal = document.querySelector(".modal-login");
  const btnCloseModal = document.querySelector(".modal-close");
  const btnsOpenModal = document.querySelectorAll(".login-info");

  const openModal = function () {
    modal.classList.remove("d-none");
    overlay.classList.remove("d-none");
  };

  const closeModal = function () {
    modal.classList.add("d-none");
    overlay.classList.add("d-none");
  };

  for (let i = 0; i < btnsOpenModal.length; i++)
    btnsOpenModal[i].addEventListener("click", openModal);

  btnCloseModal.addEventListener("click", closeModal);
  overlay.addEventListener("click", closeModal);

  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape" && !modal.classList.contains("hidden")) {
      closeModal();
    }
  });
}
