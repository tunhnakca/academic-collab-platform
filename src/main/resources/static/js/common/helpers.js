// Leaving confirmation helper function
export function enableLeavingConfirmation(
  message = "Are you sure you want to leave this page?"
) {
  window.addEventListener("beforeunload", (event) => {
    event.preventDefault();
    event.returnValue = message;
    return message;
  });
}

// Opening and closing dropdown menu
export function toggleDropdown(triggerSelector, dropdownSelector) {
  const triggerElement = document.querySelector(triggerSelector);
  const dropdown = document.querySelector(dropdownSelector);

  if (!triggerElement || !dropdown) return;

  triggerElement.addEventListener("click", function (e) {
    dropdown.classList.toggle("d-none");
  });

  document.addEventListener("click", function (e) {
    if (!triggerElement.contains(e.target) && !dropdown.contains(e.target)) {
      dropdown.classList.add("d-none");
    }
  });
}
