import { async } from "regenerator-runtime";
import { updateSectionHeight } from "../common/helpers.js";
import { overlay } from "../common/config";

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

  userList.addEventListener("click", function (e) {
    if (e.target.closest(".delete-user-btn")) {
      e.preventDefault();
      const selectedUser = e.target.closest("tr");
      const userNumber = selectedUser.dataset.userNumber;
      const courseCode = selectedUser.dataset.relatedCourse;
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
      <p>Are you sure you want to delete <em>${userName}</em> <em>${userSurname}</em> ?</p>
      <div class="modal-delete-user__buttons">
      <button class="btn btn--danger" id="confirm-delete__user">Yes</button>
      <button class="btn btn--light" id="cancel-delete__user">No</button>
      </div>
    `;

    document.body.appendChild(modal);
    overlay.classList.remove("d-none");

    document
      .getElementById("confirm-delete__user")
      .addEventListener("click", function () {
        deleteUserFromServer(userNumber, courseCode);
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

  async function deleteUserFromServer(userNumber, courseCode) {
    try {
      const response = await fetch(
        `/course/remove/user?courseCode=${courseCode}/${userNumber}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.ok) {
        location.reload();
        console.info("User removed from course successfully");
      } else {
        throw new Error("Problem removing user");
      }
    } catch (error) {
      console.error(error.message);
    }
  }
}
