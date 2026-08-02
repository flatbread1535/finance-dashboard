import PublicLayout from "./components/public/PublicLayout";
import Register from "./components/public/Register";
import Login from "./components/public/Login";
import HomePage from "./components/public/HomePage";
import Protected from "./components/dashboard-layout/Protected";
import DashboardLayout from "./components/dashboard-layout/DashboardLayout";
import Dashboard from "./components/dashboard/Dashboard";
import Transactions from "./components/transactions/Transactions";
import Budgets from "./components/budgets/Budgets";
import Goals from "./components/goals/Goals";
import Groups from "./components/groups/Groups";
import Settings from "./components/settings/Settings";

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
        element: <Register />,
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
          {
            path: "/transactions",
            element: <Transactions />,
          },
          {
            path: "/budgets",
            element: <Budgets />,
          },

          {
            path: "/goals",
            element: <Goals />,
          },

          {
            path: "/groups",
            element: <Groups />,
          },

          {
            path: "/settings",
            element: <Settings />,
          },
        ],
      },
    ],
  },
];

export default routes;
