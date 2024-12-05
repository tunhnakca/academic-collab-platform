import { enableLeavingConfirmation } from "../common/helpers.js";

// Leaving confirmation sadece bu sayfada etkinleşir
if (window.location.pathname.includes("add")) {
  enableLeavingConfirmation(
    "You have unsaved changes. Do you really want to leave?"
  );
}
