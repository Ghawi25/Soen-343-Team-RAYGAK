import axios from "axios";
import { useEffect, useState } from "react";

function House() {
  const [houseJSON, setHouseJSON] = useState("");

  // Load json house
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/house")
      .then((response) => console.log(response));
  });

  return <div>House</div>
}

export default House;
