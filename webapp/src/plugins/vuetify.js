import Vue from 'vue';
import Vuetify from 'vuetify/lib';

Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        themes: {
            light: {
                primary: '#4caf50',
                secondary: '#8bc34a',
                accent: '#00bcd4',
                error: '#f44336',
                warning: '#ff5722',
                info: '#03a9f4',
                success: '#3f51b5'
            },
        },
    },
});
