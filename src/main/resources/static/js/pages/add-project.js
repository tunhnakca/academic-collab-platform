import { setupMarkdownEditor } from "../components/markdownEditor";
import { AlertService } from "../components/alert-bar-frontend";

export function setupDateValidation() {
  const startDate = document.getElementById("projectDateCreated");
  const endDate = document.getElementById("projectDateEnd");
  const form = document.querySelector(".form-add-project");
  const submitButton = form.querySelector(".btn__add-project");

  // Check if the required fields exist in the DOM
  if (!startDate || !endDate || !form || !submitButton) {
    console.warn("Required date fields not found.");
    return;
  }

  // Create error messages
  const startDateError = document.createElement("p");
  startDateError.textContent =
    "Start date cannot be earlier than current time!";
  startDateError.classList.add("error-message");
  startDateError.style.display = "none";
  startDate.insertAdjacentElement("afterend", startDateError);

  const endDateError = document.createElement("p");
  endDateError.textContent = "End date cannot be earlier than start date!";
  endDateError.classList.add("error-message");
  endDateError.style.display = "none";
  endDate.insertAdjacentElement("afterend", endDateError);

  // Set minimum date as current time
  function getCurrentDateTime() {
    const now = new Date();
    // Format: YYYY-MM-DDTHH:mm
    return now.toISOString().slice(0, 16);
  }

  // Update minimum dates
  function updateMinDates() {
    const currentDateTime = getCurrentDateTime();
    startDate.min = currentDateTime;
    endDate.min = currentDateTime;
  }

  // Initial minimum date set
  updateMinDates();

  // Function to validate dates
  function validateDates() {
    const start = new Date(startDate.value);
    const end = new Date(endDate.value);
    const now = new Date();
    let isValid = true;

    // Reset all error states
    startDate.classList.remove("input-error");
    endDate.classList.remove("input-error");
    startDateError.style.display = "none";
    endDateError.style.display = "none";

    // Check if start date is before current time
    if (start < now) {
      startDate.classList.add("input-error");
      startDateError.style.display = "block";
      isValid = false;
    }

    // Update end date minimum value when start date changes
    endDate.min = startDate.value;

    // Check end date if start date is valid
    if (startDate.value && endDate.value) {
      if (end < start) {
        endDate.classList.add("input-error");
        endDateError.style.display = "block";
        isValid = false;
      }
    }

    // Enable/disable submit button based on validation
    submitButton.disabled = !isValid;
    return isValid;
  }

  // Prevent form submission if dates are invalid
  form.addEventListener("submit", function (event) {
    if (!validateDates()) {
      event.preventDefault(); // Stop form submission
    }
  });

  // Add event listeners to trigger validation while selecting dates
  startDate.addEventListener("input", validateDates);
  endDate.addEventListener("input", validateDates);

  // Update minimum dates every minute
  setInterval(updateMinDates, 60000); // 60000 ms = 1 minute
}

export function initializeAddProjectMarkdownEditor() {
  const form = document.querySelector(".form-add-project");
  if (!form) return;
  const textArea = form.querySelector("textarea");
  const editor = setupMarkdownEditor(textArea);

  form.addEventListener("submit", (e) => {
    e.preventDefault();

    // Ensure the project description is not empty
    if (!editor.value().trim()) {
      AlertService.showAlert("Project description is required", "error");
      editor.codemirror.focus();
      return;
    }

    // Sync to textarea before submit
    textArea.value = editor.value();
    form.submit();
  });
}
