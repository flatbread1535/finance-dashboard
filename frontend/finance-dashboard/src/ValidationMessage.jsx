const ValidationMessage = ({ message }) => {
  if (!message) {
    return null;
  }

  return (
    <small className="error-msg" role="alert">
      {message}
    </small>
  );
};

export default ValidationMessage;
