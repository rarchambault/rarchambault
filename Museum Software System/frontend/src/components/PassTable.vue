<!--Table Component for Museum Pass -->
<template>
    <v-app>
        <v-card>
            <v-list-item three-line>
                <v-list-item-content>
                    <v-card-title>My Museum Passes</v-card-title>
                </v-list-item-content>
                <AddMuseumPassForm />

            </v-list-item>
            <v-simple-table>
                <thead>
                    <tr> <!-- Table Parameters -->
                        <th class="text-left">
                            ID
                        </th>
                        <th class="text-left">
                            Date
                        </th>
                        <th class="text-left">
                            Type
                        </th>
                        <th class="text-left">
                            Price
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="museumPass in museumPasses" :key="museumPass.id">
                        <td>{{ museumPass.id }}</td>
                        <td>{{ museumPass.date }}</td>
                        <td>{{ museumPass.type }}</td>
                        <td>{{ museumPass.price }}</td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-card>
    </v-app>
</template>

<script>
import axios from 'axios'
import AddMuseumPassForm from './AddMuseumPassForm.vue'
var backendUrl = 'http://localhost:8090'

// Instantiate axios client
var AXIOS = axios.create({
    baseURL: backendUrl,
})

export default {
    name: "PassTable",
    components: { AddMuseumPassForm },

    data() {

        return {
            museumPasses: [],
            visitorEmail: ''

        };
    },

    methods: {

        //Get Email associated with pass
        getEmail() {

            if (sessionStorage.getItem("email") == "") {
                alert("Error: You need to login to view and purchase passes.")
                window.location = "/login"
                return
            }

            this.visitorEmail = sessionStorage.getItem("email")
        },

        //Gets All Passes
        getAllPasses() {

            AXIOS
                .get("/museumPass/visitor/" + this.visitorEmail)
                .then(res => res.data)
                .then(response => {
                    if (response === 'Museum Passes not found.') return;
                    this.museumPasses = response;
                })
                .catch(error => {
                    console.log(error)
                    alert("Oops! An error occurred : " + error);
                    sessionStorage.setItem("email", null)
                    sessionStorage.setItem("museum", null)
                    sessionStorage.setItem("isOwner", false)
                    sessionStorage.setItem("isEmployee", false)
                    sessionStorage.setItem("isLoggedIn", false)
                    window.location = "/login";
                });
        }
    },

    created() {
        this.getEmail()
        this.getAllPasses()
    },
}

</script>