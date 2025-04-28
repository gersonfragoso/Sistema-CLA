
echo -e "Derrubando container\n"
docker compose -f docker-compose.yml down

echo -e "Buildando nova imagem\n"
docker compose -f docker-compose.yml build --no-cache

echo -e "Deletando recursos não utilizados no docker\n"
docker system prune -f

echo -e "Subindo container\n"
docker compose -f docker-compose.yml up