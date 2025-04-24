export function setupSemesterDateValidation() {
  const startDate = document.getElementById("semesterStartDate");
  const endDate = document.getElementById("semesterEndDate");
  const submitButton = document.querySelector(".btn__add-semester");
  const form = document.querySelector(".form-add-semester");

  if (!startDate || !endDate || !submitButton || !form) {
    console.warn("Semester date fields not found.");
    return;
  }

  // Create error messages
  const endDateError = document.createElement("p");
  endDateError.textContent = "End date cannot be earlier than start date!";
  endDateError.classList.add("error-message");
  endDateError.style.display = "none";
  endDate.insertAdjacentElement("afterend", endDateError);

  // Date validation and setting min date
  function validateDates() {
    const start = new Date(startDate.value);
    const end = new Date(endDate.value);
    let valid = true;

    // Set the minimum date for endDate to the startDate value
    endDate.min = startDate.value;

    // Reset error states
    endDate.classList.remove("input-error");
    endDateError.style.display = "none";

    // If the end date is earlier than the start date, show an error
    if (end < start) {
      endDate.classList.add("input-error");
      endDateError.style.display = "block";
      valid = false;
    }

    // Disable/enable the submit button based on validation
    submitButton.disabled = !valid;

    return valid;
  }

  // Validate dates when the form is submitted
  form.addEventListener("submit", function (e) {
    if (!validateDates()) {
      e.preventDefault(); // Prevent form submission
    }
  });

  // Validate when the date inputs change
  startDate.addEventListener("input", validateDates);
  endDate.addEventListener("input", validateDates);
}
