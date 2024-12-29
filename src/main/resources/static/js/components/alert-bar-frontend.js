export class AlertService {
  static showAlert(message, type = "info") {
    // Create alert element
    const alertElement = document.createElement("div");
    alertElement.className = `alert-bar alert-bar--${type}`;

    // Create alert content
    alertElement.innerHTML = `
        <ion-icon name="alert-circle-outline" class="alert-icon"></ion-icon>
        <span class="alert-message">${message}</span>
        <ion-icon name="close-outline" class="alert-close"></ion-icon>
        <div class="alert-progress-bar"></div>
      `;

    // Set icon based on alert type
    const alertIcon = alertElement.querySelector(".alert-icon");
    if (type === "danger") {
      alertIcon.setAttribute("name", "alert-circle-outline");
    } else if (type === "success") {
      alertIcon.setAttribute("name", "checkmark-circle-outline");
    }

    // Add click event listener for close button
    const closeButton = alertElement.querySelector(".alert-close");
    closeButton.addEventListener("click", () => {
      alertElement.remove();
    });

    // Insert alert at the top of the body
    document.body.insertBefore(alertElement, document.body.firstChild);

    // Automatically remove alert after 5 seconds
    setTimeout(() => {
      if (alertElement && alertElement.parentElement) {
        alertElement.remove();
      }
    }, 5000);

    return alertElement;
  }
}
