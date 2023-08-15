<template>
    <v-card variant="tonal">
        <v-card-title>Pending Loans</v-card-title>
        <v-simple-table>
            <thead>
                <tr>
                    <th class="text-left">
                        #
                    </th>
                    <th class="text-left">
                        Artwork
                    </th>
                    <th class="text-left">
                        Borrower
                    </th>
                    <th class="text-left">
                        Start Date
                    </th>
                    <th class="text-left">
                        End Date
                    </th>
                    <th class="text-left"></th>
                    <th class="text-left"></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="loan in pendingLoans" :key="loan.id">
                    <td>{{ loan.id }}</td>
                    <td>{{ loan.artwork.name }}</td>
                    <td>{{ loan.loanee.emailAddress }}</td>
                    <td>{{ loan.startDate }}</td>
                    <td>{{ loan.endDate }}</td>
                    <td> 
                        <v-btn class="ma-2" color="success" @click="approveLoan(loan.id)">
                            Approve
                            <v-icon end icon="mdi-checkbox-marked-circle"></v-icon>
                        </v-btn>
                    </td>
                    <td> 
                        <v-btn class="ma-2" color="error" @click="rejectLoan(loan.id)">
                            Reject
                            <v-icon end icon="mdi-checkbox-marked-circle"></v-icon>
                        </v-btn>
                    </td>
                </tr>
            </tbody>
        </v-simple-table>
    </v-card>
</template>

<script>
import axios from 'axios';

export default {
    name: 'DecideLoans',

    components: {},

    data() {
        return {
            pendingLoans: [],
        }
    },

    methods: {
        getPendingLoans() {
            let options = {
                method: 'GET',
                url: `http://localhost:8090/loan/status/InReview`,
                headers: {
                    'Content-Type': 'application/json',
                },
            };

            axios.request(options)
                .then(response => response.data)
                .then(pendingLoans => {
                    this.pendingLoans = pendingLoans;
                })
                .catch(err => console.error(err));
        },

        rejectLoan(id) {
            let options = {
                method: 'POST',
                url: `http://localhost:8090/loan/reject`,
                headers: {
                    'Content-Type': 'application/json',
                },
                data: {
                    "id": id
                }
            };

            axios.request(options)
                .then(response => response.data)
                .then(() => {
                    this.getPendingLoans();       
                })
                .catch(err => {
                    console.error(err)
                    alert("Oops! An error has occurred: " + err.response.data)
                });
        },

        approveLoan(id) {
            let options = {
                method: 'POST',
                url: `http://localhost:8090/loan/approve`,
                headers: {
                    'Content-Type': 'application/json',
                },
                data: {
                    "id": id
                }
            };

            axios.request(options)
                .then(response => response.data)
                .then(() => {
                    this.getPendingLoans();       
                })
                .catch(err => {
                    console.error(err)
                    alert("Oops! An error has occurred: " + err.response.data)
                });
        }
    },

    created() {
        this.getPendingLoans();
    }
};
</script>