import { useEffect, useState } from "react";
import DashboardChart from "./DashboardChart";
import DashboardSummary from "./DashboardSummary";
import "../../styles/dashboard/Dashboard.css";

const Dashboard = () => {
  const [transactions, setTransactions] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const getTransactions = async () => {
      const token = localStorage.getItem("token");

      if (!token) {
        window.location.href = "/login";
        return;
      }

      try {
        const response = await fetch(`${import.meta.env.VITE_API_URL}/transactions`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 401) {
          localStorage.removeItem("token");
          window.location.href = "/login";
          return;
        }

        if (!response.ok) {
          throw new Error("Failed to load dashboard data.");
        }

        const data = await response.json();
        setTransactions(data.content ?? []);
        setError(null);
      } catch (err) {
        setError(err.message);
      } finally {
        setIsLoading(false);
      }
    };

    getTransactions();
  }, []);

  if (isLoading) {
    return (
      <main>
        <p>Loading dashboard...</p>
      </main>
    );
  }

  if (error) {
    return (
      <main>
        <p>{error}</p>
      </main>
    );
  }

  return (
    <main className="dashboard">
      <DashboardSummary transactions={transactions} />
      <DashboardChart transactions={transactions} />
    </main>
  );
};

export default Dashboard;
