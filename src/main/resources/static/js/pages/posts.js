import { formatDateTime, updateSectionHeight } from "../common/helpers.js";
import { setupMarkdownEditor } from "../components/markdownEditor";
import { AlertService } from "../components/alert-bar-frontend";

// Updating section user-list height
export function updateSectionPostsHeight() {
  updateSectionHeight("section-posts");
}

export function updateProjectDateTimeOnPosts() {
  // 1. Classic display for sidebar
  formatDateTime(
    ["posts__sidebar-course__information__value__date"],
    undefined,
    {
      year: "numeric",
      month: "long",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    }
  );

  // 2. "Smart" display for post messages (or comments)
  formatDateTime(["post-date"], undefined, {}, "smart");
}

export function initializePostsMarkdownEditor() {
  const editor = setupMarkdownEditor("post");
  const form = document.querySelector(".form-post");

  // Handle form submission
  if (form) {
    form.addEventListener("submit", (e) => {
      e.preventDefault();

      const textArea = document.getElementById("post");

      // Validate description
      if (!editor.value().trim()) {
        console.warn("Your answer cannot be empty");
        AlertService.showAlert("Your answer cannot be empty", "error");
        editor.codemirror.focus();
        return;
      }

      if (textArea && editor) {
        textArea.value = editor.value();
      }

      form.submit();
    });
  }
}
