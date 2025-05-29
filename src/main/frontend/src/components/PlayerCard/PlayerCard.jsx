import React, { useEffect, useState } from 'react';
import api from '../../services/api';
import './PlayerCard.css';

const PlayerCard = ({ player }) => {
    if (!player) return null;

    return (
        <div className="player-card" style={{ border: '1px solid #ccc', padding: 20, maxWidth: 300 }}>
            <img
                className="player-card-img"
                src={player.photoURL}
                alt={`${player.firstName} ${player.lastName}`}
                style={{ width: '100%', height: 'auto' }}
            />
            <h2>{player.firstName} {player.lastName}</h2>
            <p><strong>Position:</strong> {player.position}</p>
            <p><strong>Age:</strong> {player.age}</p>
            <p><strong>Nationality:</strong> {player.nationality}</p>
            <p><strong>Shirt Number:</strong> {player.shirtNumber}</p>
            <p><strong>Club:</strong> {player.clubName}</p>
        </div>
    );
};

export default PlayerCard;
