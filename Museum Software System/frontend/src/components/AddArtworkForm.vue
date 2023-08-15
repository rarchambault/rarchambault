<template>

    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-btn class="mx-2" fab dark color="indigo" v-bind="attrs" v-on="on">
                <v-icon dark>
                    mdi-plus
                </v-icon>
            </v-btn>
        </template>
        <v-card>
            <v-form ref="form">
              <!-- Title of popup form  -->
                <v-card-title>
                    <span class="text-h5">Add Artwork</span>
                </v-card-title>
                <v-card-text>
                    <v-container>

                        <v-row>
                          <!-- Labels for each column and v-model is what is written in textfield  -->
                            <v-col cols="12">
                                <v-text-field v-model="name" label="Artwork Name" :rules="[v => !!v]" required></v-text-field>
                            </v-col>

                            <v-col cols="12" sm="6" md="3">
                                <v-text-field v-model="loanFee" label="Loan Fee" type="number" prefix="$" :rules="[v => !!v]"
                                    required></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" md="4">
                                <v-select v-model="isAvailableForLoan" :items="['Yes', 'No']" label="Available for Loan" :rules="[v => !!v]"
                                    required></v-select>
                            </v-col>
                            <v-col cols="12" sm="6" md="5">
                              <!-- Select Fields  -->
                                <v-select v-model="isAvailableInMuseum" :items="['Yes', 'No']" :rules="[v => !!v]"
                                    label="Available in Museum" required></v-select>
                            </v-col>
                            <v-col cols="12">
                                <v-select v-model="room" :items="rooms" label="Artwork Location" :rules="[v => !!v]"></v-select>
                            </v-col>
                        </v-row>
                    </v-container>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>

                  <!-- Buttons to close and add  -->
                    <v-btn color="blue darken-1" text @click="dialog = false">
                        Close
                    </v-btn>
                    <v-btn color="blue darken-1" text @click="addArtwork">
                        Add
                    </v-btn>
                </v-card-actions>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>

import axios from 'axios';

export default {
    name: 'AddArtworkForm',

    // this is the inputs from the textfields and select fields, and rooms from the getRooms() method
    data: () => ({
        dialog: false,
        rooms: [],
        name: '',
        loanFee: '',
        isAvailableForLoan: '',
        isAvailableInMuseum: '',
        room: '',
        valid: true
    }),

    methods: {

        getRooms() {
            let options = {
                method: 'GET',
                url: `http://localhost:8090/room`,
                headers: {
                    'Content-Type': 'application/json',
                },
            };

            axios.request(options)
                .then(response => response.data)
                .then(rooms => {
                    this.rooms = rooms.map((room, idx) => {
                        if (room.type !== 'Storage') {
                            return {
                                value: room.id,
                                text: `Room #${idx + 1} (${room.type})`
                            }
                        } else {
                            return {
                                value: room.id,
                                text: `Storage`
                            }
                        }
                    });
                })
                .catch(err => console.error(err));
        },

        addArtwork() {

            this.valid = this.$refs.form.validate();

            if(this.valid){
                let options = {
                    method: 'POST',
                    url: `http://localhost:8090/artwork`,
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    data: {
                        museumId: sessionStorage.getItem("museum"),
                        roomId: this.room,
                        name: this.name,
                        loanFee: this.loanFee,
                        isAvailableForLoan: this.isAvailableForLoan === 'Yes' ? true : false,
                        isInMuseum: this.isAvailableInMuseum === 'Yes' ? true : false,
                        visitorOnWaitingListEmail: null
                    }
                };

                axios.request(options)
                    .then(response => response.data)
                    .then(() => {
                        this.$refs.form.reset();
                        this.$parent.$parent.$parent.getArtworks();
                        this.dialog = false;
                    })
                    .catch(err => console.error(err));
            }
        }
    },

    created() {
        this.getRooms();
    }
}
</script>