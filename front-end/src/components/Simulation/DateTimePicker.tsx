import React, { useState } from "react";

const DateTimePicker: React.FC<{
  onTemperatureFetch: (temperature: string) => void;
}> = ({ onTemperatureFetch }) => {
  const [dateTime, setDateTime] = useState("");

  const formatDateTime = (inputDateTime: string) => {
    const date = new Date(inputDateTime);
    let formattedDate = date.toISOString().split("T")[0]; // Formats date to "yyyy-mm-dd", but day is erronous
    let lastCharOfDateInNum = parseInt(
      formattedDate.charAt(formattedDate.length - 1)
    );
    const fixedDayValue = (lastCharOfDateInNum - 1).toString();

    formattedDate =
      formattedDate.substring(0, formattedDate.length - 1) + fixedDayValue; // fixed day value of date

    const hours = date.getHours();
    const minutes = date.getMinutes();
    let formattedTime = "";

    // Formats time to "HH:MM"
    if (minutes > 10) {
      formattedTime = `${hours.toString().padStart(2, "0")}:${minutes}`;
    } else formattedTime = `${hours.toString().padStart(2, "0")}:0${minutes}`; // Formats time to "HH:MM"

    return { formattedDate, formattedTime };
  };

  const handleSubmit = async () => {
    const { formattedDate, formattedTime } = formatDateTime(dateTime);
    // Construct the query parameters
    const queryParams = new URLSearchParams({
      date: formattedDate,
      time: formattedTime,
    });

    const endpoint = `http://localhost:8080/temperature?${queryParams}`;
    try {
      const response = await fetch(endpoint);
      if (response.ok) {
        const data = await response.json();
        // Assuming the server response contains a temperature value
        // setTemperature(data.temperature); // Update the state with the received temperature
        console.log("Temperature received:", data.temperature);
        onTemperatureFetch(data.temperature.toString()); // Call the handler with the new temperature
      } else {
        // Handle server errors or invalid responses
        console.error("Server error:", response.statusText);
      }
    } catch (error) {
      // Handle network errors
      console.error("Network error:", error);
    }
  };

  return (
    <div className="datetime-picker">
      <span>Date & Time:</span>
      <input
        type="datetime-local"
        value={dateTime}
        onChange={(e) => setDateTime(e.target.value)}
      />
      <button className="btn--shift-down" onClick={handleSubmit}>
        Fetch
      </button>
    </div>
  );
};

export default DateTimePicker;
