import { async } from "regenerator-runtime";
import {
  updateSectionHeight,
  getQueryParam,
  getCurrentItemsCount,
  redirectToPreviousPage,
} from "../common/helpers.js";
import { overlay } from "../common/config";
import showAlert from "../components/show-alert-bar";

export const deleteUserButton = document.querySelector(
  ".section-user-list .delete-user-btn"
);

// Updating section user-list height
export function updateSectionUserListHeight() {
  updateSectionHeight("section-user-list");
}

// Deleting user
export function deleteUser() {
  const userList = document.querySelector(".section-user-list .user-list");

  if (!deleteUserButton) {
    console.warn(`Delete button couldn't find`);
    return;
  }

  if (!userList) {
    console.warn(`User list couldn't find!`);
    return;
  }

  userList.addEventListener("click", function (e) {
    if (e.target.closest(".delete-user-btn")) {
      e.preventDefault();

      const selectedUser = e.target.closest("tr");
      const userNumber = selectedUser?.dataset?.userNumber;
      const courseCode = selectedUser?.dataset?.relatedCourse;
      const userName = selectedUser.querySelector(
        ".user-list__user-name"
      ).textContent;
      const userSurname = selectedUser.querySelector(
        ".user-list__user-surname"
      ).textContent;

      // Show modal with confirmation
      showDeleteUserModal(userNumber, userName, userSurname, courseCode);
    }
  });

  const showDeleteUserModal = function (
    userNumber,
    userName,
    userSurname,
    courseCode
  ) {
    const modal = document.createElement("div");
    modal.classList.add("modal-delete-user");
    modal.innerHTML = `
      <p>Are you sure you want to remove <em>${userName}</em> <em>${userSurname}</em> from this course ?</p>
      <div class="modal-delete-user__buttons">
      <button class="btn btn--danger" id="confirm-delete__user">Yes</button>
      <button class="btn btn--light" id="cancel-delete__user">No</button>
      </div>
    `;

    document.body.appendChild(modal);
    overlay.classList.remove("d-none");

    document
      .getElementById("confirm-delete__user")
      .addEventListener("click", async function () {
        await removeUserFromServer(userNumber, courseCode);
        modal.remove();
        overlay.classList.add("d-none");
      });

    document
      .getElementById("cancel-delete__user")
      .addEventListener("click", function () {
        modal.remove();
        overlay.classList.add("d-none");
      });
  };

  async function removeUserFromServer(userNumber, courseCode) {
    try {
      const usersCount = getCurrentItemsCount(".user-list tbody tr");
      const pageNo = getQueryParam("pageNo", "number", 0);

      const response = await fetch(
        `/api/courses/remove/user?courseCode=${courseCode}&userNumber=${userNumber}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      const data = await response.json();

      if (response.ok) {
        // Set message to localStorage
        localStorage.setItem(
          "alertMessage",
          JSON.stringify({ message: data.message, status: "success" })
        );

        // 1. If it is on the homepage/pageNo=0 then reload
        if (pageNo === 0) {
          location.reload();
          return;
        }

        // 2. If there is only one user left and he is deleted, the previous page
        if (usersCount === 1 && pageNo > 0) {
          const searchParams = new URLSearchParams(window.location.search);
          searchParams.set("pageNo", pageNo - 1);

          let baseUrl;
          if (window.location.pathname.startsWith("/users/search")) {
            baseUrl = "/users/search";
          } else {
            baseUrl = "/student/list";
          }

          redirectToPreviousPage(baseUrl, searchParams, null);
          return;
        }

        // 3. Reload in all other cases
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
