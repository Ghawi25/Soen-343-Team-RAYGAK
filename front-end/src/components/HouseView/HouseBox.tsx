import axios from "axios";
import { useEffect, useState } from "react";

var roomPositions = {};
// function to generate a rectangle for the room
const Room = ({ name, x, y, width, height, id }: any) => {
  return (
    <g key={id} id={name}>
      <rect
        x={x * width * 100}
        y={y * height * 100}
        width={width * 100}
        height={height * 100}
        fill="white"
        stroke="black"
      />
      <text
        x={x * width * 100 + width * 10}
        y={y * height * 100 + height * 50}
        font-family="Verdana"
        font-size="35"
        fill="black"
      >
        {name}
      </text>
    </g>
  );
};

// function to generate doors
const Door = ({ name, x, y, roomWidth, roomHeight, isVertical }: any) => {
  if (isVertical) {
    return (
      <g key={name} id={name}>
        <rect
          x={x * roomWidth * 100 - 5}
          y={y * roomHeight * 100 - 12.5}
          width={10}
          height={25}
          fill="rgb(139,69,19)"
        />
      </g>
    );
  }
  return (
    <g key={name} id={name}>
      <rect
        x={x * roomWidth * 100 - 12.5}
        y={y * roomHeight * 100 - 5}
        width={25}
        height={10}
        fill="rgb(139,69,19)"
      />
    </g>
  );
};
//Function to make a window
const Window = ({ name, x, y, roomWidth, roomHeight, isVertical }: any) => {
  if (isVertical) {
    return (
      <g key={name} id={name}>
        <rect
          x={x * roomWidth * 100 - 5}
          y={y * roomHeight * 100 - 12.5}
          width={10}
          height={25}
          fill="rgb(135,206,235)"
        />
      </g>
    );
  }
  return (
    <g key={name} id={name}>
      <rect
        x={x * roomWidth * 100 - 12.5}
        y={y * roomHeight * 100 - 5}
        width={25}
        height={10}
        fill="rgb(135,206,235)"
      />
    </g>
  );
};

//function to make lights
const Light = ({name, x, y, roomWidth, roomHeight}) => {
  return (
    <g key={name} id={name}>
      <circle r={5} cx={x*roomWidth*100} cy={y*roomHeight*100} fill="yellow"/>
    </g>
  )
}

// Function to render all rooms
function renderRooms(rooms: any[]) {
  // Get positions first
  roomPositions = {};
  // Start at 0, 0
  var x = 0;
  var y = 0;
  roomPositions[rooms[0].roomID] = { x: x, y: y };
  rooms.forEach((room) => {
    var currRoomPositions = roomPositions[room.roomID];
    var adjacentRoom = room.topAdjacentRoom;
    if (!(adjacentRoom === null)) {
      roomPositions[adjacentRoom.roomID] = {
        x: currRoomPositions.x,
        y: currRoomPositions.y - 1,
      };
    }
    adjacentRoom = room.bottomAdjacentRoom;
    if (!(adjacentRoom === null)) {
      roomPositions[adjacentRoom.roomID] = {
        x: currRoomPositions.x,
        y: currRoomPositions.y + 1,
      };
    }
    adjacentRoom = room.leftAdjacentRoom;
    if (!(adjacentRoom === null)) {
      roomPositions[adjacentRoom.roomID] = {
        x: currRoomPositions.x - 1,
        y: currRoomPositions.y,
      };
    }
    adjacentRoom = room.rightAdjacentRoom;
    if (!(adjacentRoom === null)) {
      roomPositions[adjacentRoom.roomID] = {
        x: currRoomPositions.x + 1,
        y: currRoomPositions.y,
      };
    }
  });
  // calculating offsets to make turn all positions positive
  var minX = Infinity;
  var minY = Infinity;
  for (let [key, value] of Object.entries(roomPositions)) {
    if (value.x < minX) minX = value.x;
    if (value.y < minY) minY = value.y;
  }

  // apply offset
  for (let [key, value] of Object.entries(roomPositions)) {
    value.x = value.x - minX;
    value.y = value.y - minY;
  }

  return rooms.map((room) => {
    const roomPosition = roomPositions[room.roomID];
    return (
      <Room
        name={room.name}
        x={roomPosition.x}
        y={roomPosition.y}
        width={room.width}
        height={room.height}
        id={room.roomID}
      />
    );
  });
}

// Render all doors
function renderDoors(rooms: any[]) {
  var doorPositions = {};

  // get offset of doors
  rooms.forEach((room) => {
    const roomPosition = roomPositions[room.roomID];
    const doors = room.doors;
    doors.forEach((door: { position: string; name: string }) => {
      if (door.position === "north") {
        doorPositions[door.name] = {
          x: roomPosition.x + 0.5,
          y: roomPosition.y,
          isVertical: false,
        };
      } else if (door.position === "south") {
        doorPositions[door.name] = {
          x: roomPosition.x + 0.5,
          y: roomPosition.y + 1,
          isVertical: false,
        };
      } else if (door.position === "east") {
        doorPositions[door.name] = {
          x: roomPosition.x + 1,
          y: roomPosition.y + 0.5,
          isVertical: true,
        };
      } else {
        doorPositions[door.name] = {
          x: roomPosition.x,
          y: roomPosition.y + 0.5,
          isVertical: true,
        };
      }
    });
  });

  // Render doors, skip if already rendered
  const added = new Set();
  return rooms.map((room) => {
    const doors = room.doors;
    return doors.map((door: { name: string }) => {
      const doorPosition = doorPositions[door.name];
      if (added.has(door.name)) return;
      added.add(door.name);
      return (
        <Door
          name={door.name}
          x={doorPosition.x}
          y={doorPosition.y}
          roomWidth={room.width}
          roomHeight={room.height}
          isVertical={doorPosition.isVertical}
        />
      );
    });
  });
}

// render windows
function renderWindows(rooms: any[]) {
  var windowPositions = {};

  rooms.map((room) => {
    const roomPosition = roomPositions[room.roomID];
    const windows = room.windows;
    // For each room, get windows per wall
    const windowsPerDirection = {
      north: [],
      south: [],
      east: [],
      west: [],
    };

    windows.forEach((window) => {
      if (window.position === "north") {
        windowsPerDirection["north"].push(window);
      } else if (window.position === "south") {
        windowsPerDirection["south"].push(window);
      } else if (window.position === "east") {
        windowsPerDirection["east"].push(window);
      } else {
        windowsPerDirection["west"].push(window);
      }
    });

    // Get positions of windows
    let i = 1;
    windowsPerDirection["south"].forEach((window) => {
      windowPositions[window.windowID] = {
        x: roomPosition.x + (i * 1) / (windowsPerDirection["south"].length + 1),
        y: roomPosition.y + 1,
        isVertical: false,
      };
      i++;
    });
    i = 1;
    windowsPerDirection["north"].forEach((window) => {
      windowPositions[window.windowID] = {
        x: roomPosition.x + (i * 1) / (windowsPerDirection["north"].length + 1),
        y: roomPosition.y,
        isVertical: false,
      };
      i++;
    });
    i = 1;
    windowsPerDirection["east"].forEach((window) => {
      windowPositions[window.windowID] = {
        x: roomPosition.x + 1,
        y: roomPosition.y + (i * 1) / (windowsPerDirection["east"].length + 1),
        isVertical: true,
      };
      i++;
    });
    i = 1;
    windowsPerDirection["west"].forEach((window) => {
      windowPositions[window.windowID] = {
        x: roomPosition.x,
        y: roomPosition.y + (i * 1) / (windowsPerDirection["west"].length + 1),
        isVertical: true,
      };
      i++;
    });
    console.log(windowPositions);
  });
  // Rendering the windows:
  const added = new Set();
  return rooms.map((room) => {
    const windows = room.windows;
    return windows.map((window: { windowID: string }) => {
      const windowPosition = windowPositions[window.windowID];
      if (added.has(window.windowID)) return;
      added.add(window.windowID);
      return (
        <Window
          name={window.windowID}
          x={windowPosition.x}
          y={windowPosition.y}
          roomWidth={room.width}
          roomHeight={room.height}
          isVertical={windowPosition.isVertical}
        />
      );
    });
  });
}

// render lights
function renderLights(rooms) {
  return rooms.map((room) => { 
    const roomPosition = roomPositions[room.roomID];
    return <Light name={room.roomID + "Light"} x={roomPosition.x + 0.5} y={roomPosition.y + 0.5} roomHeight={room.height} roomWidth={room.width}/>
  })
}

function HouseBox() {
  const [house, setHouse] = useState(null);

  // Get house information
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/house")
      .then((res) => setHouse(res.data))
      .catch((err) => console.log(err));
  }, []);

  if (!house) {
    return <div>Loading...</div>;
  } else {
    return (
      <svg className="houseBox" width="2000" height="2000">
        {renderRooms(house)}
        {renderDoors(house)}
        {renderWindows(house)}
        {renderLights(house)}
      </svg>
      // <img src="houseView.jpg" alt="houseView" />
    );
  }
}

export default HouseBox;
