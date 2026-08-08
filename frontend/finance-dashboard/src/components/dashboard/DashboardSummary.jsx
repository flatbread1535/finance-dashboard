import "../../styles/dashboard/DashboardSummary.css";

const formatCurrency = (amount) =>
  new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
  }).format(amount);

const getMetrics = (transactions) => {
  const successful = transactions.filter(
    (transaction) => transaction.status !== "FAILED",
  );

  const failedCount = transactions.length - successful.length;

  let income = 0;
  let expenses = 0;
  const categoryTotals = {};

  for (const transaction of successful) {
    const amount = Number(transaction.amount);
    const category = transaction.category || "Uncategorized";

    if (amount > 0) {
      income += amount;
    } else {
      expenses += Math.abs(amount);
    }

    categoryTotals[category] =
      (categoryTotals[category] || 0) + Math.abs(amount);
  }

  let topCategory = "No activity yet";
  let topCategoryValue = 0;

  for (const [category, total] of Object.entries(categoryTotals)) {
    if (total > topCategoryValue) {
      topCategory = category;
      topCategoryValue = total;
    }
  }

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
          <p>
            {topCategory} ({formatCurrency(topCategoryValue)})
          </p>
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
