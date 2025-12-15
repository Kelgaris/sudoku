// Backend Express para nuestro juego del Sudoku
// Donde vamos a guardar y obtener las puntuaciones de los jugadores

require('dotenv').config({ path: __dirname + '/.env' });
const express = require('express');
const cors = require('cors');
const { MongoClient} = require('mongodb');

const app = express();
app.use(cors());
app.use(express.json());

const uri = process.env.MONGO_URI;
const port = process.env.PORT || 3000;



// Endpoint para guardar la puntuacion de un jugador
app.post('/puntuaciones',async(req, res)=>{
    const client = new MongoClient(uri);
    const nuevaPuntuacion = req.body;
    const {nombre, tiempo, nivel} = nuevaPuntuacion;

    if(!nombre || !tiempo || !nivel){
        return res.status(400).json({error: 'Faltan datos obligatorios'});
    }

    try{
        await client.connect();
        const collection = client.db('sudoku').collection('Puntuaciones');
        const resultado = await collection.insertOne(nuevaPuntuacion);
        res.status(201).json(resultado);
    }catch(error){
        res.status(500).json({error: 'Error al guardar la puntuacion'});
    }
})


//Endpoint para obtener las puntuaciones de los jugadores filtradas por nivel y ordenadas por tiempo
app.get('/puntuaciones/:nivel', async(req, res)=>{
    const client = new MongoClient(uri);
    const nivel = req.params.nivel;
    if(!nivel){
        return res.status(400).json({error: 'Falta el parametro nivel'});
    }
    try{
        await client.connect();
        const collection = client.db('sudoku').collection('Puntuaciones');
        const puntuaciones = await collection.find({nivel}).sort({tiempo: 1}).toArray();
        res.status(200).json(puntuaciones);
    }catch(error){
        console.log(error)
        res.status(500).json({error: 'Error al obtener las puntuaciones'});
    }
})

app.listen(port, ()=>{
    console.log(`Servidor escuchando en el puerto ${port}`);
});
