import React, { useEffect, useState } from 'react';
import { useApi } from './hooks/useApi';
import { playerService } from './services/playerService';
import BoxForHintIMG from './components/BoxForHintIMG/BoxForHintIMG';
import BoxForHintString from './components/BoxForHintString/BoxForHintString';
import './styles/main.css'

function App() {
    const { loading, error, execute } = useApi();
    const [player, setPlayer] = useState(null);
    const [guess, setGuess] = useState('');
    const [inputValue, setInputValue] = useState('');
    const [guessResult, setGuessResult] = useState(null);
    const [isGuessing, setIsGuessing] = useState(false);
    const [revealedHints, setRevealedHints] = useState({
        nationality: false,
        league: false,
        club: false,
        position: false,
        number: false
    });

    useEffect(() => {
        execute(() => playerService.getRandomPlayer())
            .then(data => setPlayer(data))
            .catch(() => {
            });
    }, [execute]);

    useEffect(() => {
        const timeoutId = setTimeout(() => {
            setGuess(inputValue);
        }, 500); // 500ms delay

        return () => clearTimeout(timeoutId);
    }, [inputValue]);

    const handleGuess = async () => {
        const currentGuess = inputValue.trim() || guess.trim();
        if (!currentGuess) {
            alert('Please enter a guess!');
            return;
        }

        if (!player) {
            alert('No player loaded yet!');
            return;
        }

        setIsGuessing(true);
        try {
            // Properly handle spaces and special characters in the guess
            const sanitizedGuess = currentGuess.replace(/\s+/g, ' ').trim();
            console.log('Sending request with:', {
                randomPlayer: player,
                guess: sanitizedGuess
            });

            const result = await execute(() => playerService.checkGuess(player, sanitizedGuess));
            setGuessResult(result);
            console.log('Guess result:', result);

            // Handle the result based on your backend response
            if (result.correct) {
                alert('Correct! Well done!');
                // You might want to start a new game or show success state
            } else {
                alert('Incorrect guess. Try again!');
                // Optionally reveal a hint or handle incorrect guess
            }
        } catch (error) {
            console.error('Error checking guess:', error);
            console.error('Error details:', error.response?.data || error.message);
            alert(`Error checking your guess: ${error.response?.data?.message || error.message}. Please try again.`);
        } finally {
            setIsGuessing(false);
        }

        setInputValue('');
        setGuess('');
    };

    const handleInputChange = (e) => {
        setInputValue(e.target.value);
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleGuess();
        }
    };

    return (
        <div className="main">
            {player ? (
                <>
                    <div className="imageContainerWithHint">
                        <div className="questionMark">?</div>
                        <div className="imageContainer">
                            <img className="PlayerIMG" src={player.photoURL} alt={player.name} />
                        </div>
                    </div>
                    <div className="containerForHints">
                        <BoxForHintIMG
                            category="Nationality"
                            src={player.nationality.logoURL}
                            alt={player.nationality.name}
                            isRevealed={revealedHints.nationality}
                        />
                        <BoxForHintIMG
                            category="League"
                            src={player.club.league.logoURL}
                            alt={player.club.league.name}
                            isRevealed={revealedHints.league}
                        />
                        <BoxForHintIMG
                            category="Club"
                            src={player.club.logoURL}
                            alt={player.club.name}
                            isRevealed={revealedHints.club}
                        />
                        <BoxForHintString
                            category="Position"
                            value={player.position}
                            isRevealed={revealedHints.position}
                        />
                        <BoxForHintString
                            category="Number"
                            value={player.shirtNumber}
                            isRevealed={revealedHints.number}
                        />
                    </div>
                    <div className="containerForInput">
                        <input
                            className="input"
                            placeholder="Insert player name/country/club/league"
                            value={inputValue}
                            onChange={handleInputChange}
                            onKeyPress={handleKeyPress}
                            disabled={isGuessing}
                        />
                        <button
                            className="button"
                            onClick={handleGuess}
                            disabled={isGuessing}
                        >
                            {isGuessing ? 'Checking...' : 'Guess'}
                        </button>
                    </div>
                    {guessResult && (
                        <div className="guessResult">
                            {/* Display guess result here based on your backend response */}
                            <p>Last guess result: {guessResult.correct ? 'Correct!' : 'Incorrect'}</p>
                        </div>
                    )}
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