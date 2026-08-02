import "../../styles/transactions/TransactionsTable.css";

const TransactionsTable = ({ transactions, onCreate, onEdit, onDelete }) => {
  return (
    <section className="table-container">
      <div className="table-header">
        <h2>Your Transactions</h2>
        <button type="button" onClick={onCreate}>
          Add Transaction
        </button>
      </div>

      {transactions.length === 0 ? (
        <p className="empty-table">No transactions yet.</p>
      ) : (
        <div className="table-scroll">
          <table className="transaction-table">
            <caption>List of all transactions</caption>
            <thead>
              <tr>
                <th scope="col">Date</th>
                <th scope="col">Category</th>
                <th scope="col">Amount</th>
                <th scope="col">Status</th>
                <th scope="col">Actions</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((transaction) => (
                <tr key={transaction.transactionId}>
                  <td>
                    {new Date(transaction.timeCreated).toLocaleDateString()}
                  </td>
                  <td>{transaction.category}</td>
                  <td>${Number(transaction.amount).toFixed(2)}</td>
                  <td>{transaction.status}</td>
                  <td className="transaction-options">
                    <button
                      type="button"
                      className="edit-btn"
                      onClick={() => onEdit(transaction)}
                    >
                      Edit
                    </button>
                    <button
                      type="button"
                      className="delete-btn"
                      onClick={() => onDelete(transaction)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
};

export default TransactionsTable;
