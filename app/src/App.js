import React, { useState } from 'react';
import './App.css';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';


const App = () => {
  const [output, setOutput] = useState([]);
  
  const handleAction = (action) => {
    setOutput([...output, action]);
  };

  const [homeFeature,setHomeFeature] = useState('SHS');
  const handleHomeFeature = (feature) => {
    setHomeFeature(feature);
  }

  return (
    <>
    <header className="app-header">
    Smart Home Simulator
  </header>
    <div className="App">
    
    <div className="sidebar"><Sidebar /></div>
    <div className="container">
      <div style={{flexDirection:'column', display:'flex',width:'100vh'}}>
        <div style={{flexDirection:'row', display:'flex',width:'100vh',height:'80%'}}>
          <div className="panel-container">
            <FeatureNav onHomeFeature={handleHomeFeature} />
            <ControlPanel onAction={handleAction} homeFeature={homeFeature} />
          </div>
        <HouseView />
        </div>
        <OutputConsole output={output} />
      </div>
    </div>
  </div>
  </>
    
  );
};



const Sidebar = () => {
  const [startDate, setStartDate] = useState(new Date());

  const [timeSpeed, setTimeSpeed] = useState(1);

  const handleTimeSpeedChange = (timeValue) => {
    setTimeSpeed(timeValue.target.value);
  };

  return (
    <>  
      {<>
        <div className="toggle-switch">
      <label>
        <input type="checkbox" />
        <span className="slider round"></span>
      </label>
      </div>
      <div className="profile">
        <div className="avatar">
          
        </div>
      <div className="profile-text">
        <p>Parent</p>
        <p>Location: </p>
        <p>Kitchen</p>
      </div>
      </div>
      <div className="temperature">
        <p>Outside Temp: 15°C</p>
      </div></>}
      <div className="datetime-picker">
        <DatePicker
          selected={startDate}
          onChange={(date) => setStartDate(date)}
          showTimeSelect
          timeFormat="HH:mm"
          timeIntervals={15}
          timeCaption="time"
          dateFormat="MMMM d, yyyy h:mm aa"
        />
      </div>
      <div className="time-speed">
      <p>Time speed</p>
      <input
          type="range"
          id="timeSpeed"
          name="timeSpeed"
          min="1"
          max="2"
          step="0.1"
          value={timeSpeed}
          onChange={handleTimeSpeedChange}
        />
    </div>
    </>
  );
};


const ControlPanel = ({ onAction, homeFeature }) => {
  const [selectedItem, setSelectedItem] = useState(null);

  const handleSelectItem = (item) => {
    setSelectedItem(item);
  };

  let controls;
  switch (homeFeature) {
    case 'SHS':
      controls = <div>SHS Controls here</div>;
      break;
    case 'SHC':
      const items = ['Windows', 'Lights', 'Doors'];
      controls = (
        <>
          <div className="item-list">
            <div style={{ borderBottom: '1px solid black', textAlign: 'center', color: 'blue' }}>Item</div>
            {items.map((item, index) => (
              <div
                key={index}
                className={`item ${selectedItem === item ? 'selected' : ''}`}
                onClick={() => handleSelectItem(item)}
              >
                <label><input className="itembutton"type="button" value={item}/></label>
              </div>
            ))}
          </div>
          <div className='item-location'>
  <p style={{textAlign:'center', color:'lightblue'}}>Open/Close</p>
  <div style={{border:'1px solid', padding: '5px'}}>
    <label style={{display: 'block'}}>
      <input type='checkbox' name='backyard' 
        onClick={() => onAction(`The ${selectedItem} for the Backyard was toggled`)}
      />
      Backyard
    </label>
    <label style={{display: 'block'}}>
      <input type='checkbox' name='garage' 
        onClick={() => onAction(`The ${selectedItem} for the Garage was toggled`)}
      />
      Garage
    </label>
    <label style={{display: 'block'}}>
      <input type='checkbox' name='main' 
        onClick={() => onAction(`The ${selectedItem} for the Main was toggled`)}
      />
      Main
    </label>
    <p style={{color:'blue', textDecoration:'underline', cursor: 'pointer'}}>All / None</p>
  </div>
</div>
      </>
      );
      break;
    case 'SHP':
      controls = <div>SHP Controls here</div>;
      break;
    case 'SHH':
      controls = <div>SHH Controls here</div>;
      break;
    case 'ADD':
      controls = <div>ADD Controls here</div>;
      break;
    default:
      controls = <div>Default Controls here</div>;
  }
  
  return (
    <div className="control-panel">
      {controls}
    </div>
  );
};



const HouseView = () => (
  <div className="house-view">
    <div className="houseBox">
      {}
    </div>
    <h5 style={{textAlign:'center'}}>House View</h5>
  </div>
);

const FeatureNav = ({onHomeFeature}) => {
  return (
    <div className="feature-nav">
      <button onClick={() => onHomeFeature('SHS')}>SHS</button>
      <button onClick={() => onHomeFeature('SHC')}>SHC</button>
      <button onClick={() => onHomeFeature('SHP')}>SHP</button>
      <button onClick={() => onHomeFeature('SHH')}>SHH</button>
      <button onClick={() => onHomeFeature('ADD')}>+</button>
    </div>
  )
}


const OutputConsole = ({ output }) => (
  <div className="output-console">
    <h5 style={{textAlign:'center'}}>Output Console</h5>
    <div className= "outputBox">
    {output.map((line, index) => (
      <div key={index} className="console-line">{line}</div>
    ))}
    </div>
  </div>
);

export default App;