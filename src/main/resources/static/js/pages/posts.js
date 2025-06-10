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
  // Find all answer forms on the page
  document.querySelectorAll(".form-post").forEach((form) => {
    const textArea = form.querySelector("textarea");
    const editor = setupMarkdownEditor(textArea);

    form.addEventListener("submit", (e) => {
      e.preventDefault();

      // Ensure the answer is not empty
      if (!editor.value().trim()) {
        AlertService.showAlert("Your answer cannot be empty", "error");
        editor.codemirror.focus();
        return;
      }

      // Copy markdown editor value into original textarea before submit
      textArea.value = editor.value();
      form.submit();
    });
  });
}

export function initializeReplyMarkdownEditor() {
  // Find all reply forms on the page
  document.querySelectorAll(".form-reply").forEach((form) => {
    const textArea = form.querySelector("textarea");
    const editor = setupMarkdownEditor(textArea);

    form.addEventListener("submit", (e) => {
      e.preventDefault();

      // Ensure the reply is not empty
      if (!editor.value().trim()) {
        AlertService.showAlert("Your reply cannot be empty", "error");
        editor.codemirror.focus();
        return;
      }

      // Copy markdown editor value into original textarea before submit
      textArea.value = editor.value();
      form.submit();
    });
  });
}

export function initializeReplyScrollToForm() {
  // Select the reply button in project-info
  const projectInfoReplyBtn = document.querySelector(
    ".project-info .post-actions__item--reply"
  );
  const replyForm = document.getElementById("post-form");

  if (!projectInfoReplyBtn || !replyForm) return;

  if (projectInfoReplyBtn && replyForm) {
    projectInfoReplyBtn.addEventListener("click", function (e) {
      e.preventDefault();

      // Scroll to form
      replyForm.scrollIntoView({
        behavior: "smooth",
        block: "center",
      });

      // Focus on CodeMirror's textarea
      setTimeout(() => {
        // Select CodeMirror's textarea directly
        const cmTextarea = document.querySelector(".CodeMirror textarea");

        if (!cmTextarea) return;

        if (cmTextarea) {
          cmTextarea.focus();
          cmTextarea.click();
        }
      }, 600);
    });
  } else {
    return;
  }
}

// This function toggles (shows/hides) replies when either Reply or Replies button is clicked.
export function initializeRepliesToggle() {
  document
    .querySelectorAll(
      ".post-actions__item--reply__parent-post, .post-actions__item--toggle-replies"
    )
    .forEach(function (btn) {
      btn.addEventListener("click", function (e) {
        // Only proceed if data-post-id exists (skip project-info/general reply button)
        const postId = btn.dataset.postId;

        if (!postId) return;

        // Find the closest .post-wrapper (the answer container)
        const postWrapper = btn.closest(".post-wrapper");
        if (!postWrapper) return;

        // Find the replies container inside this answer
        const repliesDiv = postWrapper.querySelector(".post-replies");
        if (!repliesDiv) return;

        // Prevent default button behavior (form submission, etc.)
        e.preventDefault();

        // Toggle the display: if hidden, show; if shown, hide
        if (
          repliesDiv.style.display === "none" ||
          repliesDiv.style.display === ""
        ) {
          repliesDiv.style.display = "block";
        } else {
          repliesDiv.style.display = "none";
        }
      });
    });
}
