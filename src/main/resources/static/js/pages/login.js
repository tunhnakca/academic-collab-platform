import { overlay } from "../common/config";

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
