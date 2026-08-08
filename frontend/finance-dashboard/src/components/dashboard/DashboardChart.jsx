import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Pie } from "react-chartjs-2";

import "../../styles/dashboard/DashboardChart.css";

ChartJS.register(ArcElement, Tooltip, Legend);

const getCategoryTotals = (transactions, matchesAmount) => {
  const totals = {};

  for (const transaction of transactions) {
    const amount = Number(transaction.amount);

    if (transaction.status === "FAILED" || !matchesAmount(amount)) {
      continue;
    }

    const category = transaction.category || "Uncategorized";

    totals[category] = (totals[category] || 0) + Math.abs(amount);
  }

  return totals;
};

const getColors = (count) =>
  Array.from(
    { length: count },
    (_, i) => `hsl(${(i * 360) / count}, 65%, 55%)`,
  );

const CategoryPieChart = ({ title, transactions, matchesAmount }) => {
  const categoryTotals = getCategoryTotals(transactions, matchesAmount);

  const labels = Object.keys(categoryTotals);
  const values = Object.values(categoryTotals);

  const data = {
    labels,
    datasets: [
      {
        data: values,
        backgroundColor: getColors(labels.length),
        borderColor: "#ffffff",
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "bottom",
        labels: {
          boxWidth: 10,
          boxHeight: 10,
          color: "#425570",
          padding: 8,
          font: { size: 8 },
        },
      },
      tooltip: {
        callbacks: {
          label: (context) => `$${Number(context.raw || 0).toFixed(2)}`,
        },
      },
    },
  };

  return (
    <div className="chart-title-container">
      <h2>{title}</h2>
      <div className="chart-container">
        {labels.length > 0 ? (
          <Pie data={data} options={options} />
        ) : (
          <div className="no-activity">No transaction activity yet.</div>
        )}
      </div>
    </div>
  );
};

const DashboardChart = ({ transactions = [] }) => (
  <section className="dashboard-charts">
    <CategoryPieChart
      title="Expenses by category"
      transactions={transactions}
      matchesAmount={(amount) => amount < 0}
    />
    <CategoryPieChart
      title="Deposits by category"
      transactions={transactions}
      matchesAmount={(amount) => amount > 0}
    />
  </section>
);

export default DashboardChart;
