(() => {
  document.addEventListener("DOMContentLoaded", () => {
    const alertBar = document.querySelector(".alert-bar");
    const alertIcon = alertBar?.querySelector(".alert-icon");
    const closeButton = alertBar?.querySelector(".alert-close");

    // Automatically remove the alert after 5 seconds
    if (alertBar) {
      setTimeout(() => {
        alertBar.parentElement.removeChild(alertBar);
      }, 5000);

      // Close the alert when the close button is clicked
      closeButton?.addEventListener("click", () => {
        alertBar.parentElement.removeChild(alertBar);
      });
    }

    // Change the icon if the alert has the "alert-bar--danger" class
    if (alertBar?.classList.contains("alert-bar--danger"))
<<<<<<< HEAD
      alertIcon?.setAttribute("name", "alert-circle-outline");
=======
      alertIcon?.setAttribute("name", "close-circle-outline");
>>>>>>> main

    // Change the icon if the alert has the "alert-bar--success" class
    if (alertBar?.classList.contains("alert-bar--success"))
      alertIcon?.setAttribute("name", "checkmark-circle-outline");
  });
})();
