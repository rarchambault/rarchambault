<template>
    <v-card variant="tonal">
        <v-list-item three-line>
            <v-list-item-content>
                <v-card-title>Artworks Overview</v-card-title>
            </v-list-item-content>
            <AddArtworkForm />
        </v-list-item>
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
                        Loan Fee
                    </th>
                    <th class="text-left">
                        Available for Loan
                    </th>

                    <th class="text-left">
                        Available in Museum
                    </th>
                    <th class="text-left">
                        Location
                    </th>
                    <th width="150px"></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="artwork in artworks" :key="artwork.id">
                    <td>{{ artwork.id }}</td>
                    <td>{{ artwork.name }}</td>
                    <td>{{ artwork.loanFee }}</td>
                    <td>{{ artwork.isAvailableForLoan === true ? 'Yes' : 'No' }}</td>
                    <td>{{ artwork.isInMuseum === true ? 'Yes' : 'No' }}</td>
                    <td>{{ getRoomDisplayName(artwork.room.id) }}</td>
                    <td>
                        <EditArtworkFrom :artwork="artwork"/>
                        <v-btn class="ma-2 error" outlined x-small fab color="white" @click="deleteArtwork(artwork.id)">
                            <v-icon>mdi-trash-can</v-icon>
                        </v-btn>
                    </td>
                </tr>
            </tbody>
        </v-simple-table>
    </v-card>
</template>

<script>

import axios from 'axios';
import AddArtworkForm from './AddArtworkForm';
import EditArtworkFrom from './EditArtworkForm';

export default {
    name: 'ArtworksOverview',

    components: { AddArtworkForm, EditArtworkFrom },

    data() {
        return {
            artworks: [],
            rooms: []
        }
    },

    methods: {
        getArtworks() {
            let options = {
                method: 'GET',
                url: `http://localhost:8090/artwork`,
                headers: {
                    'Content-Type': 'application/json',
                }
            };

            axios.request(options)
                .then(response => response.data)
                .then(artworks => {
                    this.artworks = artworks;
                })
                .catch(err => console.error(err));
        },

        getRoomDisplayName(id) {
            return this.rooms.find(room => room.value === id).text;
        },

        deleteArtwork(id){
            let options = {
                method: 'DELETE',
                url: `http://localhost:8090/artwork/id/${id}`,
                headers: {
                    'Content-Type': 'application/json',
                },
            };

            axios.request(options)
                .then(response => response.data)
                .then(() => {
                    this.getArtworks();
                })
                .catch(err => console.error(err));
        },

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
        }
    },

    created() {
        this.getRooms();
        this.getArtworks();
    },
};
</script>

<style>
.v-text-field__details {
    display: none;
}

.v-input__slot {
    margin-bottom: 0;
}
</style>