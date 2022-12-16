import axios from 'axios'

const options = {
	baseURL: "http://localhost:8080",
	headers: {
	}
}

export default {
    install: (app) => {
        app.config.globalProperties.$axios = axios.create(options)
    }
}