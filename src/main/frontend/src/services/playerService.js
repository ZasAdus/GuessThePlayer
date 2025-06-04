import api from './api';

export const playerService = {

    getRandomPlayer: async () => {
        try {
            const response = await api.get('/players/random');
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch random player');
        }
    },

    getPlayerById: async (id) => {
        try {
            const response = await api.get(`/players/${id}`);
            return response.data;
        } catch (error) {
            throw new Error(`Failed to fetch player with ID ${id}`);
        }
    },

    getPlayerByFullName: async (fullName) => {
        try {
            const response = await api.post(`/players/fullName=${fullName}`);
            return response.data;
        } catch (error) {
            throw new Error(`Failed to fetch player with ID ${fullName}`);
        }
    },

    checkGuess: async (randomPlayer, guess) => {
        try {
            console.log('Sending guess request:', { randomPlayer, guess });
            const response = await api.post('/players/guess', {
                randomPlayer: randomPlayer,
                guess: guess
            });
            console.log('Guess response:', response.data);
            return response.data;
        } catch (error) {
            console.error('API error details:', error.response?.data);
            console.error('Full error:', error);

            // Provide more specific error information
            if (error.response?.status === 404) {
                throw new Error('Guess endpoint not found. Check if backend is running on correct port.');
            } else if (error.response?.status === 500) {
                throw new Error(`Server error: ${error.response.data?.message || 'Internal server error'}`);
            } else if (error.response?.status === 400) {
                throw new Error(`Bad request: ${error.response.data?.message || 'Invalid request format'}`);
            } else if (error.code === 'ECONNREFUSED') {
                throw new Error('Cannot connect to backend. Check if server is running on port 8080.');
            } else {
                throw new Error(`Failed to check guess: ${error.response?.data?.message || error.message}`);
            }
        }
    }
};