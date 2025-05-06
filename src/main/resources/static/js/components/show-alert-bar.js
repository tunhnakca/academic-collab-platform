export default function showAlert(message, type = "success") {
  const alertBar = document.querySelector(".alert-bar");
  if (!alertBar) return;

  const alertIcon = alertBar?.querySelector(".alert-icon");
  const closeButton = alertBar?.querySelector(".alert-close");
  const alertMessage = alertBar.querySelector(".alert-message");

  // Set the message
  if (alertMessage) alertMessage.textContent = message;

  // Set the class according to its type
  alertBar.classList.remove("alert-bar--error", "alert-bar--success");
  alertBar.classList.add(
    type === "error" ? "alert-bar--error" : "alert-bar--success"
  );

  // Set the icon
  alertIcon.setAttribute(
    "name",
    type === "error" ? "alert-circle-outline" : "checkmark-circle-outline"
  );

  alertBar.classList.remove("d-none");

  // Automatically remove the alert after 5 seconds

  setTimeout(() => {
    alertBar.classList.add("d-none");
  }, 5000);

  // Close the alert when the close button is clicked
  closeButton?.addEventListener("click", () => {
    alertBar.classList.add("d-none");
  });
}
