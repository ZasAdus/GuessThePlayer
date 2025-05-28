import api from './api';

export const playerService = {
    getAllPlayers: async () => {
        try {
            const response = await api.get('/players');
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch players');
        }
    },

    getRandomPlayer: async () => {
        try {
            const response = await api.get('/players/random');
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch random player');
        }
    },

    submitGuess: async (playerId, guessedName) => {
        try {
            const response = await api.post('/players/guess', {
                playerId,
                guessedName,
                timestamp: new Date().toISOString()
            });
            return response.data;
        } catch (error) {
            throw new Error('Failed to submit guess');
        }
    },

    getPlayerById: async (id) => {
        try {
            const response = await api.get(`/players/${id}`);
            return response.data;
        } catch (error) {
            throw new Error(`Failed to fetch player with ID ${id}`);
        }
    }
};