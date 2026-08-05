import "../../styles/dashboard/DashboardSummary.css";

const formatCurrency = (amount) =>
  new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
  }).format(amount);

const getMetrics = (transactions) => {
  const successful = transactions.filter((t) => t.status !== "FAILED");
  const failedCount = transactions.length - successful.length;

  let income = 0;
  let expenses = 0;
  const categoryTotals = {};

  successful.forEach(({ amount, category }) => {
    const value = Number(amount) || 0;

    if (value > 0) {
      income += value;
    } else {
      expenses += Math.abs(value);
    }

    const key = category || "Uncategorized";
    categoryTotals[key] = (categoryTotals[key] || 0) + Math.abs(value);
  });

  const [topCategory, topCategoryValue] = Object.entries(categoryTotals).sort(
    (a, b) => b[1] - a[1],
  )[0] || ["No activity yet", 0];

  return {
    income,
    expenses,
    net: income - expenses,
    successfulCount: successful.length,
    failedCount,
    topCategory,
    topCategoryValue,
  };
};

const DashboardSummary = ({ transactions = [] }) => {
  const {
    income,
    expenses,
    net,
    successfulCount,
    failedCount,
    topCategory,
    topCategoryValue,
  } = getMetrics(transactions);

  const cards = [
    { title: "Net balance", value: formatCurrency(net) },
    { title: "Income", value: formatCurrency(income) },
    { title: "Expenses", value: formatCurrency(expenses) },
    { title: "Successful transactions", value: successfulCount },
  ];

  return (
    <section className="dashboard-summary">
      <h2>Transactions Summary</h2>

      <div className="card-container">
        {cards.map((card) => (
          <div className="card" key={card.title}>
            <p className="card-title">{card.title}</p>
            <p className="card-value">{card.value}</p>
          </div>
        ))}
      </div>

      <div className="bottom-info">
        <div className="bottom-info-piece">
          <h3>Top category:</h3>
          <p>{topCategory} ({formatCurrency(topCategoryValue)})</p>
        </div>
        <div className="bottom-info-piece">
          <h3>Failed transactions:</h3>
          <p>{failedCount}</p>
        </div>
      </div>
    </section>
  );
};

export default DashboardSummary;
