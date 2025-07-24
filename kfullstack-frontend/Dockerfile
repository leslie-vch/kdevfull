# Usa una imagen base de Node.js
FROM node:18-alpine

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia los archivos de tu proyecto al contenedor
COPY package*.json ./

# Instala las dependencias del proyecto
RUN npm install

# Copia el resto de los archivos del proyecto
COPY . .

# Construye la aplicación Next.js
RUN npm run build

# Expone el puerto 3000 para acceder a la app
EXPOSE 3000

# Comando para iniciar la aplicación en modo producción
CMD ["npm", "start"]
