<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-btn class="ma-2" outlined x-small fab color="indigo" v-bind="attrs" v-on="on">
                <v-icon>mdi-pencil</v-icon>
            </v-btn>
        </template>
        <v-card>
            <v-card-title>
                <span class="text-h5">Edit Artwork</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-row>
                        <v-col cols="12">
                            <v-text-field label="Artwork Name" v-model="name" required></v-text-field>
                        </v-col>

                        <v-col cols="12" sm="6" md="3">
                            <v-text-field label="Loan Fee" v-model="loanFee" type="number" prefix="$"
                                required>
                            </v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" md="4">
                            <v-select :items="['Yes', 'No']"
                                v-model="isAvailableForLoan"
                                label="Available for Loan" required>
                            </v-select>
                        </v-col>
                        <v-col cols="12" sm="6" md="5">
                            <v-select 
                                :items="['Yes', 'No']" 
                                v-model="isAvailableInMuseum" 
                                label="Available in Museum" required>
                            </v-select>
                        </v-col>
                        <v-col cols="12">
                            <v-select 
                                :items="rooms" 
                                v-model="room"
                                label="Artwork Location">
                            </v-select>
                        </v-col>
                    </v-row>
                </v-container>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="dialog = false">
                    Close
                </v-btn>
                <v-btn color="blue darken-1" text @click="updateArtwork">
                    Save
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>

import axios from 'axios';

export default {
    name: 'AddArtworkForm',

    props: {
        artwork: Object
    },

    data: () => ({
        dialog: false,
        rooms: [],
        name: '',
        loanFee: '',
        isAvailableForLoan: '',
        isAvailableInMuseum: '',
        room: ''
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

        updateArtwork() {
            let options = {
                method: 'POST',
                url: `http://localhost:8090/artwork/update`,
                headers: {
                    'Content-Type': 'application/json',
                },
                data: {
                    id: this.artwork.id,
                    museumId: sessionStorage.getItem("museum"),
                    roomId: this.room,
                    name: this.name,
                    loanFee: this.loanFee,
                    isAvailableForLoan: this.isAvailableForLoan === 'Yes' ? true : false,
                    isInMuseum: this.isAvailableInMuseum === 'Yes' ? true : false,
                    visitorOnWaitingListEmail: null
                }
            };

            console.log(options.data);
            axios.request(options)
                .then(response => response.data)
                .then(() => {
                    this.$parent.$parent.$parent.getArtworks();
                    this.dialog = false;
                })
                .catch(err => console.error(err));
        }
    },



    created() {
        this.getRooms();

        this.name = this.artwork.name;
        this.loanFee = this.artwork.loanFee;
        this.isAvailableForLoan = this.artwork.isAvailableForLoan === true ? 'Yes' : 'No';
        this.isAvailableInMuseum = this.artwork.isInMuseum === true ? 'Yes' : 'No';
        this.room = this.artwork.room.id;

    }
}
</script>