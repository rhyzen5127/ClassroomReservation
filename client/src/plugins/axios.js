import axios from 'axios'

const options = {
	baseURL: "http://192.168.1.38:8080",
	headers: {
		"Access-Control-Allow-Headers": [ "Authentication", "Accept", "Accept-Language", "Content-Language", "Content-Type" ]
	}
}

export default axios.create(options)