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

////////////////////////////////////////
// Updating DateTimeFormat
export function formatDateTime(
  classNames,
  locale = navigator.language,
  options = {},
  mode = "default" // "smart" or "default"
) {
  const now = new Date();
  classNames.forEach((className) => {
    const elements = document.querySelectorAll(`.${className}`);
    elements.forEach((el) => {
      const dateStr = el.dataset.date;
      if (!dateStr) return;
      const date = new Date(dateStr);

      if (mode === "smart") {
        // Check the difference by day (hours/minutes are not important)
        const isToday =
          date.getDate() === now.getDate() &&
          date.getMonth() === now.getMonth() &&
          date.getFullYear() === now.getFullYear();

        // To find yesterday's day...
        const yesterday = new Date(now);
        yesterday.setDate(now.getDate() - 1);
        const isYesterday =
          date.getDate() === yesterday.getDate() &&
          date.getMonth() === yesterday.getMonth() &&
          date.getFullYear() === yesterday.getFullYear();

        let timeStr = date
          .toLocaleTimeString(locale, {
            hour: "2-digit",
            minute: "2-digit",
            hour12: false,
          })
          .replace(/^0/, ""); // unnecessary leading zero

        if (isToday) {
          el.textContent =
            timeStr + " , " + (locale.startsWith("tr") ? "Bugün" : "Today");
        } else if (isYesterday) {
          el.textContent =
            timeStr + " , " + (locale.startsWith("tr") ? "Dün" : "Yesterday");
        } else {
          // Older history
          el.textContent =
            timeStr +
            " , " +
            date.toLocaleDateString(locale, {
              day: "2-digit",
              month: "2-digit",
              year: "numeric",
            });
        }
      } else {
        // classic: continue the old method
        const formatter = new Intl.DateTimeFormat(locale, options);
        el.textContent = formatter.format(date);
      }
    });
  });
}

////////////////////////////////////////
// Updating Section Height
export function updateSectionHeight(sectionClassName) {
  const header = document.querySelector("header");
  const sectionEl = document.querySelector(`.${sectionClassName}`);

  if (!header || !sectionEl) return;

  const headerHeight = header.offsetHeight;
  sectionEl.style.height = `calc(100vh - ${headerHeight}px`;
}
