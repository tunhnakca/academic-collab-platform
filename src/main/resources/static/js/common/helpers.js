////////////////////////////////////////
// Leaving confirmation helper function
export function enableLeavingConfirmation() {
  window.addEventListener("beforeunload", (e) => {
    e.preventDefault();
    e.returnValue = message;
  });
}

////////////////////////////////////////
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

////////////////////////////////////////
// Showing message if empty
export function showEmptyMessage(containerSelector, message) {
  const container = document.querySelector(containerSelector);

  if (!container) {
    return;
  }

  if (container.children.length === 0) {
    const parentEl = container.parentElement;
    const messageEl = document.createElement("p");
    messageEl.textContent = message;
    messageEl.classList.add("empty-message");
    parentEl.prepend(messageEl.cloneNode(true));
  }
}
