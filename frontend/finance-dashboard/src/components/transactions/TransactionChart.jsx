import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
} from "chart.js";
import { Line } from "react-chartjs-2";
import "../../styles/transactions/TransactionChart.css";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
);

const TransactionChart = ({ transactions = [] }) => {
  const sortedTransactions = [...transactions].sort(
    (a, b) => new Date(a.timeCreated) - new Date(b.timeCreated),
  );

  const labels = sortedTransactions.map((transaction) =>
    new Date(transaction.timeCreated).toLocaleDateString(),
  );

  const balanceData = sortedTransactions.reduce((acc, transaction) => {
    const previousBalance = acc.length > 0 ? acc[acc.length - 1] : 0;
    const amount =
      transaction.status === "FAILED" ? 0 : Number(transaction.amount);

    acc.push(previousBalance + amount);
    return acc;
  }, []);

  const data = {
    labels,
    datasets: [
      {
        label: "Balance Over Time",
        data: balanceData,
        borderColor: "#ff8200",
        backgroundColor: "#ff8200",
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        labels: {
          color: "#425570",
        },
      },
    },
    scales: {
      x: {
        grid: {
          color: "#425570",
          lineWidth: 2,
        },
        ticks: {
          color: "#425570",
          font: {
            size: 16,
            weight: "bold",
          },
        },
        border: {
          color: "#425570",
          width: 2,
        },
      },
      y: {
        grid: {
          color: "#425570",
          lineWidth: 2,
        },
        ticks: {
          color: "#425570",
          font: {
            size: 13,
            weight: "bold",
          },
        },
        border: {
          color: "#425570",
          width: 2,
        },
      },
    },
  };

  return (
    <section className="transaction-chart">
      <Line data={data} options={options} />
    </section>
  );
};

export default TransactionChart;
