import { async } from "regenerator-runtime";
import { overlay } from "../common/config";
import showAlert from "../components/show-alert-bar";
import {
  formatDateTime,
  updateSectionHeight,
  getQueryParam,
  getCurrentItemsCount,
  redirectToPreviousPage,
  getDataSetInfo,
} from "../common/helpers.js";
import { setupMarkdownEditor } from "../components/markdownEditor";
import { AlertService } from "../components/alert-bar-frontend";

// Updating section posts height
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
  document.querySelectorAll(".form-reply").forEach((form) => {
    const textArea = form.querySelector("textarea");
    const editor = setupMarkdownEditor(textArea);
    const repliedToNumberInput = form.querySelector(
      'input[name="repliedToNumber"]'
    );

    // Store the original/default value
    const defaultRepliedToNumber = repliedToNumberInput
      ? repliedToNumberInput.getAttribute("th:value") ||
        repliedToNumberInput.defaultValue ||
        repliedToNumberInput.value
      : null;

    // Store current protected mention
    let protectedMention = null;

    // Create a custom overlay for protected mention
    if (editor && editor.codemirror) {
      const cm = editor.codemirror;

      // Function to update protected mention display
      function updateProtectedMention(mentionNumber) {
        cm.setValue(""); // Clear first

        if (mentionNumber) {
          protectedMention = `@${mentionNumber}`;
          const mentionText = `@${mentionNumber} `;
          cm.setValue(mentionText);

          // Mark the mention as read-only with custom styling
          cm.markText(
            { line: 0, ch: 0 },
            { line: 0, ch: mentionText.length - 1 },
            {
              className: "protected-mention",
              readOnly: true,
              atomic: true,
              inclusiveLeft: true,
              inclusiveRight: false,
            }
          );

          // Position cursor after the mention
          cm.setCursor({ line: 0, ch: mentionText.length });
        } else {
          protectedMention = null;
          cm.setValue("");
        }
      }

      // Listen for form attribute changes (when reply buttons are clicked)
      form.addEventListener("data-mention-update", function (e) {
        const newMention = e.detail.mention;
        const isParentReply = e.detail.isParentReply;

        if (isParentReply) {
          // Parent reply - clear everything
          repliedToNumberInput.value = defaultRepliedToNumber;
          updateProtectedMention(null);
        } else if (newMention) {
          // Child reply - set protected mention
          repliedToNumberInput.value = newMention;
          updateProtectedMention(newMention);
        }
      });

      // Prevent deletion of protected mention
      cm.on("beforeChange", function (cm, change) {
        if (protectedMention && change.origin !== "setValue") {
          const from = change.from;
          const to = change.to;

          // Check if trying to modify the protected mention area
          if (from.line === 0 && from.ch < protectedMention.length + 1) {
            change.cancel();
          }
        }
      });
    }

    form.addEventListener("submit", (e) => {
      e.preventDefault();

      if (!editor.value().trim()) {
        AlertService.showAlert("Your reply cannot be empty", "error");
        editor.codemirror.focus();
        return;
      }

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
        const cmTextarea = document.querySelector(
          ".form-post .CodeMirror textarea"
        );

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

/**
 * Handles the reply system functionality for posts.
 * - Toggle button: Shows/hides replies section with chevron icon update
 * - Reply buttons: Opens replies, scrolls to form, and handles @ mentions for nested replies
 * Uses event delegation for dynamic content handling.
 */
export function initializeRepliesToggle() {
  document
    .querySelector(".container-posts")
    .addEventListener("click", function (e) {
      const btn = e.target.closest(
        ".post-actions__item--reply__parent-post, .post-actions__item--reply__child-post, .post-actions__item--toggle-replies"
      );
      if (!btn) return;

      const postWrapper = btn.closest(".post-wrapper");
      if (!postWrapper) return;

      const repliesDiv = postWrapper.querySelector(".post-replies");
      if (!repliesDiv) return;

      e.preventDefault();

      // Helper functions
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

      // Toggle replies visibility
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
      // Handle reply button clicks
      else if (
        btn.classList.contains("post-actions__item--reply__parent-post") ||
        btn.classList.contains("post-actions__item--reply__child-post")
      ) {
        if (!isRepliesOpen()) {
          openReplies();
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

        const replyForm = postWrapper.querySelector(".form-reply");
        if (!replyForm) return;

        // Trigger custom event to update mention
        if (btn.classList.contains("post-actions__item--reply__child-post")) {
          const replyDiv = btn.closest(".post--reply");
          if (replyDiv) {
            const replyUserNumber = replyDiv.dataset.replyUserNumber;
            replyForm.dispatchEvent(
              new CustomEvent("data-mention-update", {
                detail: { mention: replyUserNumber, isParentReply: false },
              })
            );
          }
        } else {
          // Parent reply - clear mention
          replyForm.dispatchEvent(
            new CustomEvent("data-mention-update", {
              detail: { mention: null, isParentReply: true },
            })
          );
        }

        // Scroll and focus
        replyForm.scrollIntoView({ behavior: "smooth", block: "center" });
        setTimeout(() => {
          const cmTextarea = replyForm.querySelector(".CodeMirror textarea");
          if (cmTextarea) {
            cmTextarea.focus();
            cmTextarea.click();
          }
        }, 600);
      }
    });
}

export function deletePostOrReply() {
  const postsContainer = document.querySelector(".container-posts");

  if (!postsContainer) {
    return;
  }

  postsContainer.addEventListener("click", function (e) {
    const deleteBtn = e.target.closest(".post-actions__item--delete");
    if (!deleteBtn) return;

    e.preventDefault();

    const postId = deleteBtn.dataset.postId;

    showDeletePostModal(postId);
  });

  function showDeletePostModal(postId) {
    const modal = document.createElement("div");
    modal.classList.add("modal-delete-post");
    modal.innerHTML = `
      <p>Are you sure you want to delete this post/reply? This action cannot be undone!</p>
      <div class="modal-delete-post__buttons">
        <button class="btn btn--danger" id="confirm-delete__post">Yes</button>
        <button class="btn btn--light" id="cancel-delete__post">No</button>
      </div>
    `;

    document.body.appendChild(modal);
    overlay.classList.remove("d-none");

    // Yes, delete!
    modal
      .querySelector("#confirm-delete__post")
      .addEventListener("click", async function () {
        await removePostFromServer(postId);
        modal.remove();
        overlay.classList.add("d-none");
      });
    // No!
    modal
      .querySelector("#cancel-delete__post")
      .addEventListener("click", function () {
        modal.remove();
        overlay.classList.add("d-none");
      });
  }

  async function removePostFromServer(postId) {
    try {
      const postsCount = getCurrentItemsCount(".post-wrapper");
      const pageNo = getQueryParam("pageNo", "number", 0);
      const projectId = getDataSetInfo(".section-posts", "projectId");

      const response = await fetch(`/api/post/delete?postId=${postId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      });

      const data = await response.json();

      if (response.ok) {
        // Use localStorage for flash message, then reload or redirect as needed
        localStorage.setItem(
          "alertMessage",
          JSON.stringify({ message: data.message, status: "success" })
        );

        // 1. If we are on the home page or ?pageNo=0: reload
        if (pageNo === 0) {
          location.reload();
          return;
        }

        // 2. If there is only 1 post left and pageNo > 0: go to previous page
        if (postsCount === 1 && pageNo > 0) {
          const searchParams = new URLSearchParams(window.location.search);
          searchParams.set("pageNo", pageNo - 1);

          let baseUrl;

          if (window.location.pathname.includes("/post/search/")) {
            baseUrl = "/post/search";
          } else {
            baseUrl = "/post";
          }

          redirectToPreviousPage(baseUrl, searchParams, projectId);
          return;
        }

        // 3. Other cases: reload
        location.reload();
      } else {
        showAlert(data.message || "Unexpected error", "error");
      }
    } catch (error) {
      console.error(error.message);
      showAlert("Network error or server unreachable", "error");
    }
  }
}
