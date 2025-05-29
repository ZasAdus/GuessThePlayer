import React, { useEffect, useState } from 'react';
import { useApi } from './hooks/useApi';
import { playerService } from './services/playerService';
import BoxForHintIMG from './components/BoxForHintIMG/BoxForHintIMG';
import BoxForHintString from './components/BoxForHintString/BoxForHintString';
import './styles/main.css'

function App() {
    const { loading, error, execute } = useApi();
    const [player, setPlayer] = useState(null);

    useEffect(() => {
    execute(() => playerService.getRandomPlayer())
        .then(data => setPlayer(data))
        .catch(() => {
        });
    }, [execute]);

    return (
        <div className="main">
            {player ? (
                <>
                    <div className="imageContainer">
                        <img className="PlayerIMG" src={player.photoURL} alt={player.name} />
                    </div>
                    <div className="containerForHints">
                        <BoxForHintIMG category="Nationality" src={player.nationality.logoURL} alt={player.name} />
                        <BoxForHintIMG category="League" src={player.club.league.logoURL} alt={player.name} />
                        <BoxForHintIMG category="Club" src={player.club.logoURL} alt={player.name} />
                        <BoxForHintString category="Position" value = {player.position}></BoxForHintString>
                        <BoxForHintString category="Number" value = {player.shirtNumber}></BoxForHintString>
                    </div>
                    <div className="containerForInput">
                        <input className="input" placeholder="Insert player name/country/club/league"/>
                        <button className="button">Guess</button>
                    </div>
                </>
            ) : (
                <div className="loadingOverlay">
                    <div className="loader"></div>
                    <p>Loading, please wait...</p>
                </div>
            )}
        </div>
    );
}

export default App;