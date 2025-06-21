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
            timeStr + ", " + (locale.startsWith("tr") ? "Bugün" : "Today");
        } else if (isYesterday) {
          el.textContent =
            timeStr + ", " + (locale.startsWith("tr") ? "Dün" : "Yesterday");
        } else {
          // Older history
          el.textContent =
            timeStr +
            ", " +
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

////////////////////////////////////////
// Getting Query Param from URL
/**
 * @param {string} key - Query param key (e.g., 'pageNo', 'keyword')
 * @param {'string' | 'number' | 'boolean'} type - Desired type of value
 * @param {*} defaultValue - Value to return if param is missing or invalid
 * @returns {*} The parsed value or the default
 */
export function getQueryParam(key, type = "string", defaultValue = null) {
  const params = new URLSearchParams(window.location.search);
  const rawValue = params.get(key);

  if (rawValue === null) return defaultValue;

  switch (type) {
    case "number":
      const num = Number(rawValue);
      return isNaN(num) ? defaultValue : num;

    case "boolean":
      return rawValue === "true" || rawValue === "1";

    case "string":
    default:
      return rawValue;
  }
}
////////////////////////////////////////
// Getting Current Items Count from that Page
export function getCurrentItemsCount(selector) {
  return document.querySelectorAll(selector).length;
}

////////////////////////////////////////
// Redirecting to Previous Page
export function redirectToPreviousPage(baseUrl, searchParams, mainId) {
  let url = baseUrl;
  if (mainId) url += `/${mainId}`;
  url += `?${searchParams.toString()}`;
  window.location.href = url;
}

////////////////////////////////////////
// Getting Data Set Info
export function getDataSetInfo(selector, dataKey) {
  const element = document.querySelector(selector);
  if (!element) return null;

  // Convert camelCase -> kebab-case to match data-* attributes if needed
  const normalizedKey = dataKey.replace(/([A-Z])/g, "-$1").toLowerCase();
  return (
    element.dataset[dataKey] || element.getAttribute(`data-${normalizedKey}`)
  );
}

////////////////////////////////////////
// Showing Modal
export function setupModal({
  modalSelector,
  openBtnSelector,
  closeBtnSelector = ".modal-close",
  overlaySelector = ".overlay",
}) {
  const modal = document.querySelector(modalSelector);
  const overlay = document.querySelector(overlaySelector);
  const btnClose = modal.querySelector(closeBtnSelector);
  const btnsOpen = document.querySelectorAll(openBtnSelector);

  if (!modal || !btnClose || !overlay) return;

  const openModal = () => {
    modal.classList.remove("d-none");
    overlay.classList.remove("d-none");
  };

  const closeModal = () => {
    modal.classList.add("d-none");
    overlay.classList.add("d-none");
  };

  btnsOpen.forEach((btn) =>
    btn.addEventListener("click", function (e) {
      e.preventDefault();
      openModal();
    })
  );
  btnClose.addEventListener("click", closeModal);
  overlay.addEventListener("click", closeModal);

  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape" && !modal.classList.contains("d-none")) {
      closeModal();
    }
  });
}
