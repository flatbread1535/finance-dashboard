import { useEffect, useCallback, useMemo, useState } from "react";

import TransactionsTable from "./TransactionsTable";
import TransactionModal from "./TransactionModal";
import TransactionChart from "./TransactionChart";

import "../../styles/transactions/Transaction.css";

const Transactions = () => {
  const [transactions, setTransactions] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  const [modal, setModal] = useState({
    isOpen: false,
    mode: null,
    transaction: null,
  });

  useEffect(() => {
    const getTransactions = async () => {
      const token = localStorage.getItem("token");

      if (!token) {
        window.location.href = "/login";
        return;
      }

      try {
        const response = await fetch("http://localhost:8080/transactions", {
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
          throw new Error("Failed to load transactions.");
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

  const fetchTransactions = useCallback(async () => {
    const token = localStorage.getItem("token");

    if (!token) {
      window.location.href = "/login";
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/transactions", {
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
        throw new Error("Failed to load transactions.");
      }

      const data = await response.json();

      setTransactions(data.content ?? []);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const sortedTransactions = useMemo(() => {
    return [...transactions].sort(
      (a, b) => new Date(b.timeCreated) - new Date(a.timeCreated),
    );
  }, [transactions]);

  const openCreateModal = () =>
    setModal({
      isOpen: true,
      mode: "create",
      transaction: null,
    });

  const openEditModal = (transaction) =>
    setModal({
      isOpen: true,
      mode: "edit",
      transaction,
    });

  const openDeleteModal = (transaction) =>
    setModal({
      isOpen: true,
      mode: "delete",
      transaction,
    });

  const closeModal = () =>
    setModal({
      isOpen: false,
      mode: null,
      transaction: null,
    });

  if (isLoading) {
    return (
      <main className="transactions-container">
        <p>Loading transactions...</p>
      </main>
    );
  }

  if (error) {
    return (
      <main className="transactions-container">
        <p>{error}</p>

        <button
          type="button"
          onClick={() => {
            setIsLoading(true);
            setError(null);
            fetchTransactions();
          }}
        >
          Retry
        </button>
      </main>
    );
  }

  return (
    <main className="transactions-container">
      <TransactionsTable
        transactions={sortedTransactions}
        onCreate={openCreateModal}
        onEdit={openEditModal}
        onDelete={openDeleteModal}
      />
      <TransactionChart transactions={transactions} />

      {modal.isOpen && (
        <TransactionModal
          mode={modal.mode}
          transaction={modal.transaction}
          onClose={closeModal}
          onSuccess={fetchTransactions}
        />
      )}
    </main>
  );
};

export default Transactions;
