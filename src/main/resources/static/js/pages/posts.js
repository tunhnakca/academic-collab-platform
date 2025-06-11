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
  const replyForm = document.querySelector(".form-post");

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
    .querySelector(".container-posts")
    .addEventListener("click", function (e) {
      // Only continue if one of the Reply/Replies toggle buttons was clicked
      const btn = e.target.closest(
        ".post-actions__item--reply__parent-post, .post-actions__item--toggle-replies"
      );
      if (!btn) return;

      // Find the closest post-wrapper (the answer's container)
      const postWrapper = btn.closest(".post-wrapper");
      if (!postWrapper) return;

      // Find the replies container within this answer
      const repliesDiv = postWrapper.querySelector(".post-replies");
      if (!repliesDiv) return;

      e.preventDefault();

      // Helpers: show/hide replies and set is-open class accordingly
      function openReplies() {
        repliesDiv.classList.remove("d-none");
        repliesDiv.classList.add("d-block");
        postWrapper.classList.add("is-open");
      }
      function closeReplies() {
        repliesDiv.classList.add("d-none");
        repliesDiv.classList.remove("d-block");
        postWrapper.classList.remove("is-open");
      }
      function isRepliesOpen() {
        return !repliesDiv.classList.contains("d-none");
      }

      // If the "Replies" toggle button (left chevron) was clicked, toggle (show/hide) replies and update the icon
      if (btn.classList.contains("post-actions__item--toggle-replies")) {
        const icon = btn.querySelector(
          ".post-actions__item--toggle-replies__icon"
        );
        if (!isRepliesOpen()) {
          openReplies();
          if (icon) icon.setAttribute("name", "chevron-up-outline");
        } else {
          closeReplies();
          if (icon) icon.setAttribute("name", "chevron-down-outline");
        }
      }
      // If the right-side "Reply" button was clicked, only open (never close) the replies; update icon if necessary
      else if (
        btn.classList.contains("post-actions__item--reply__parent-post")
      ) {
        if (!isRepliesOpen()) {
          openReplies();
          // If there's a toggle button, update the chevron icon to up
          const toggleBtn = postWrapper.querySelector(
            ".post-actions__item--toggle-replies"
          );
          if (toggleBtn) {
            const icon = toggleBtn.querySelector(
              ".post-actions__item--toggle-replies__icon"
            );
            if (icon) icon.setAttribute("name", "chevron-up-outline");
          }
        }
      }
    });
}
