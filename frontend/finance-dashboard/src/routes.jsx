import PublicLayout from "./components/PublicLayout";
import Register from "./components/Register";
import Login from "./components/Login";
import HomePage from "./components/HomePage";
import Protected from "./components/Protected";
import DashboardLayout from "./components/DashboardLayout";
import Dashboard from "./components/Dashboard";

// TODO: Add more imports from other components

// TODO: Add public routes for homepage, login, registration
// TODO: Add protected routes for a dashboard layout that has main page,
// transactions, budgets, goals, and settings
const routes = [

  // Public routes
  {
    element: <PublicLayout />,
    children: [
      {
        path: "/",
        element: <HomePage />,
      },
      {
        path: "/register",
        element: <Register />
      },
      {
        path: "/login",
        element: <Login />,
      },
    ],
  },
  // Protected Routes
  {
    element: <Protected />,
    children: [
      {
        element: <DashboardLayout />,
        children: [
          {
            path: "/dashboard",
            element: <Dashboard />,
          },
        ],
      },
    ],
  },
];

export default routes;
