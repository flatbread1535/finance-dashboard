import { useState } from "react";
import ValidationMessage from "../../ValidationMessage";
import "../../styles/transactions/TransactionModal.css";

const API_BASE = "http://localhost:8080/transactions";
const STATUSES = ["PENDING", "COMPLETED", "FAILED"];
const TRANSACTION_TYPES = ["WITHDRAWAL", "DEPOSIT"];
const CATEGORY_MAX_LENGTH = 50;

const TransactionModal = ({ mode, transaction, onClose, onSuccess }) => {
  const initialFormData = {
    amount: transaction?.amount ? Math.abs(transaction.amount) : "",
    transactionType: transaction?.amount < 0 ? "WITHDRAWAL" : "DEPOSIT",
    status: transaction?.status ?? "PENDING",
    category: transaction?.category ?? "",
  };

  const [formData, setFormData] = useState(initialFormData);

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");

  const handleChange = ({ target: { name, value } }) => {
    setFormData((prev) => ({ ...prev, [name]: value }));
    setErrors((prev) => ({ ...prev, [name]: undefined }));
  };

  const validate = (data) => {
    const nextErrors = {};

    if (!data.amount) {
      nextErrors.amount = "Transaction amount cannot be empty.";
    } else if (Number.isNaN(Number(data.amount))) {
      nextErrors.amount = "Transaction amount must be a valid number.";
    }

    const trimmedCategory = data.category.trim();

    if (!trimmedCategory) {
      nextErrors.category = "Transaction category cannot be blank.";
    } else if (trimmedCategory.length > CATEGORY_MAX_LENGTH) {
      nextErrors.category = `Category must be no more than ${CATEGORY_MAX_LENGTH} characters.`;
    }

    return nextErrors;
  };

  const getHeaders = () => ({
    "Content-Type": "application/json",
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  });

  const handleUnauthorized = () => {
    localStorage.removeItem("token");
    window.location.href = "/login";
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const validationErrors = validate(formData);

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setIsSubmitting(true);
    setError("");

    const isEdit = mode === "edit";
    const url = isEdit ? `${API_BASE}/${transaction.transactionId}` : API_BASE;
    const method = isEdit ? "PUT" : "POST";

    const amountValue = Math.abs(Number(formData.amount));
    const amount =
      formData.transactionType === "WITHDRAWAL" ? -amountValue : amountValue;

    try {
      const response = await fetch(url, {
        method,
        headers: getHeaders(),
        body: JSON.stringify({
          amount,
          currency: "USD",
          status: formData.status,
          category: formData.category.trim(),
        }),
      });

      if (response.status === 401) {
        handleUnauthorized();
        return;
      }

      if (!response.ok) {
        throw new Error("Failed to save transaction.");
      }

      onSuccess();
      onClose();
    } catch (err) {
      setError(err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = async () => {
    setIsSubmitting(true);
    setError("");

    try {
      const response = await fetch(`${API_BASE}/${transaction.transactionId}`, {
        method: "DELETE",
        headers: getHeaders(),
      });

      if (response.status === 401) {
        handleUnauthorized();
        return;
      }

      if (!response.ok) {
        throw new Error("Failed to delete transaction.");
      }

      onSuccess();
      onClose();
    } catch (err) {
      setError(err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div
        className="modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="modal-title"
        onClick={(event) => event.stopPropagation()}
      >
        {mode === "delete" ? (
          <>
            <h2 id="modal-title">Delete Transaction</h2>
            <p>Are you sure you want to delete this transaction?</p>

            {error && (
              <p className="modal-error-msg" role="alert">
                {error}
              </p>
            )}

            <div className="modal-actions">
              <button type="button" className="cancel-btn" onClick={onClose}>
                Cancel
              </button>
              <button
                type="button"
                className="delete-confirm-btn"
                onClick={handleDelete}
                disabled={isSubmitting}
              >
                {isSubmitting ? "Deleting..." : "Delete"}
              </button>
            </div>
          </>
        ) : (
          <>
            <h2 id="modal-title">
              {mode === "edit" ? "Edit Transaction" : "Add Transaction"}
            </h2>

            <form onSubmit={handleSubmit} className="modal-form" noValidate>
              <label htmlFor="amount">Amount</label>
              <input
                id="amount"
                name="amount"
                type="number"
                step="0.01"
                min="0.01"
                value={formData.amount}
                onChange={handleChange}
              />
              <ValidationMessage message={errors.amount} />

              <label htmlFor="transactionType">Transaction Type</label>
              <select
                id="transactionType"
                name="transactionType"
                value={formData.transactionType}
                onChange={handleChange}
              >
                {TRANSACTION_TYPES.map((type) => (
                  <option key={type} value={type}>
                    {type}
                  </option>
                ))}
              </select>

              <label htmlFor="status">Status</label>
              <select
                id="status"
                name="status"
                value={formData.status}
                onChange={handleChange}
              >
                {STATUSES.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>

              <label htmlFor="category">Category</label>
              <input
                id="category"
                name="category"
                type="text"
                maxLength={CATEGORY_MAX_LENGTH}
                value={formData.category}
                onChange={handleChange}
              />
              <ValidationMessage message={errors.category} />

              {error && (
                <p className="modal-error-msg" role="alert">
                  {error}
                </p>
              )}

              <div className="modal-actions">
                <button type="button" className="cancel-btn" onClick={onClose}>
                  Cancel
                </button>
                <button
                  type="submit"
                  className="modal-submit-btn"
                  disabled={isSubmitting}
                >
                  {isSubmitting ? "Saving..." : "Save"}
                </button>
              </div>
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default TransactionModal;
