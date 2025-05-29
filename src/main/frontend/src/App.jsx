import React, { useEffect, useState } from 'react';
import { useApi } from './hooks/useApi';
import { playerService } from './services/playerService';
import PlayerCard from './components/PlayerCard/PlayerCard';

function App() {
    const { loading, error, execute } = useApi();
    const [player, setPlayer] = useState(null);

    useEffect(() => {
        execute(() => playerService.getPlayerById(6))
            .then(data => setPlayer(data))
            .catch(() => {
            });
    }, [execute]);

    return (
        <div className="App" style={{ padding: 20 }}>

            {loading && <p>Loading player...</p>}
            {error && <p style={{ color: 'red' }}>Error: {error}</p>}

            {!loading && !error && player && <PlayerCard player={player} />}
        </div>
    );
}

export default App;