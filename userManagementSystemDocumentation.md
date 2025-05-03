
# 📘 User Management System – Full Project Documentation

This is a **User Management System** built as part of **Module 08**. It is a single-page web app written in **HTML**, **Tailwind CSS**, and **JavaScript**, designed to manage users efficiently. This document provides a clear and complete overview of the project structure, features, logic, and implementation.

---

## 🧩 What This Project Does

The app allows you to:
- Add new users
- Edit existing users
- Delete users (with a confirmation modal)
- Search/filter users
- Fetch random users from an external API
- Store user data persistently using `localStorage`

---

## ✅ Module 08 Requirements (All Completed)

| Feature | Status |
|--------|--------|
| Add/Edit/Delete users |
| Use `localStorage` |
| Search users |
| Use confirmation modal |
| Fetch users from external API |
| Use Tailwind CSS for styling |

---

## 🔍 File Overview

```
📂 Project Folder
├── index.html        # Main HTML structure
├── styles.css        # Tailwind CSS utilities
├── script.js         # Core JavaScript logic (DOM + logic)
```

---

## 🛠 How It Works (Feature Breakdown)

### 1. 📝 Adding Users
- Form collects user data: name, email, phone, and optional avatar URL.
- If the avatar is not provided, a default image is used.
- When submitted:
  - The user is added to the array
  - Saved to `localStorage`
  - Displayed on the screen dynamically

**Related Functions in script.js:**
```js
addUser(name, email, phone, avatar)
```

---

### 2. 🔁 Editing Users
- Clicking the **Edit** button fills the form with the selected user's data.
- After changes, clicking **Update** saves the changes:
  - Updates the array
  - Updates `localStorage`
  - Re-renders the user list

**Key Logic:**
```js
editUser(index)
updateUser(index, newData)
```

---

### 3. ❌ Deleting Users with Modal
- Clicking **Delete** opens a confirmation modal.
- On confirming, the user is removed from:
  - Array
  - `localStorage`
  - DOM

**Logic Includes:**
```js
openDeleteModal(index)
confirmDelete(index)
```

---

### 4. 🔍 Searching Users
- Typing in the search bar filters the list in real-time.
- Case-insensitive and matches name/email/phone.

**Example:**
```js
filterUsers(query)
```

---

### 5. 🌐 Fetching Random Users (External API)
- Button: **"Fetch Random Users"**
- Calls `https://randomuser.me/api/?results=5`
- Displays fetched users separately from local ones
- Includes loading animation and error handling

**Function:**
```js
fetchRandomUsers()
```

---

### 6. 🧠 Storing Users in `localStorage`
- All users (add/edit/delete) are persisted using `localStorage`
- Automatically loads saved users on page load

**Functions:**
```js
saveToLocalStorage()
loadFromLocalStorage()
```

---

### 7. 🎨 Styling and UX
- Fully styled using **Tailwind CSS**
- Responsive layout
- Smooth animations for:
  - Modals
  - User cards
  - Notifications

---

## ✨ Example Screens

- 📇 **User List**: Displays users with their information.
- 🔍 **Filter Results**: Shows the real-time search results.
- 🧍 **Random User Cards**: Fetched users with a default avatar.
- 🛑 **Modal Confirmations**: For user deletions.

---

## 📌 Summary

This project successfully demonstrates:
- **DOM Manipulation**: Handling HTML elements and events
- **Array Operations (CRUD)**: Adding, editing, and deleting users in an array
- **`localStorage` Usage**: Storing and retrieving data across sessions
- **Working with APIs**: Fetching data from an external service
- **UI Feedback**: Modals, notifications, and real-time updates
