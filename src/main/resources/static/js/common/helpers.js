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
